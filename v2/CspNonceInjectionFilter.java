package com.varicent.security.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CspNonceInjectionFilter implements Filter {

	private static final Logger LOG = LoggerFactory.getLogger(CspNonceInjectionFilter.class);

	private static final Pattern SCRIPT_TAG = Pattern.compile("<script\\b([^>]*)>", Pattern.CASE_INSENSITIVE);

	private static final Pattern HAS_SRC = Pattern.compile("\\bsrc\\s*=", Pattern.CASE_INSENSITIVE);
	private static final Pattern HAS_NONCE = Pattern.compile("\\bnonce\\s*=", Pattern.CASE_INSENSITIVE);

	private long maxBufferBytes = 32L * 1024L * 1024L;

	@Override
	public void init(FilterConfig config) throws ServletException {
		String configured = config.getInitParameter("maxBufferBytes");
		if (configured != null && !configured.trim().isEmpty()) {
			try {
				this.maxBufferBytes = Long.parseLong(configured.trim());
			} catch (NumberFormatException e) {
				LOG.warn("Ignoring unparseable maxBufferBytes '{}'", configured);
			}
		}
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		if (!(servletRequest instanceof HttpServletRequest) || !(servletResponse instanceof HttpServletResponse)) {
			chain.doFilter(servletRequest, servletResponse);
			return;
		}

		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		CapturingResponse wrapper = new CapturingResponse(response, this.maxBufferBytes);
		boolean completed = false;
		try {
			chain.doFilter(request, wrapper);
			completed = true;
		} finally {

			Object nonce = request.getAttribute(SecurityHeadersFilter.NONCE_ATTRIBUTE);
			String value = nonce == null ? null : String.valueOf(nonce);
			if (completed) {
				wrapper.finish(value);
			} else {

				try {
					wrapper.finish(value);
				} catch (Exception e) {
					LOG.warn("Could not flush buffered HTML after a failed request: {}", e.getMessage());
				}
			}
		}
	}

	@Override
	public void destroy() {
	}

	static String addNonces(String html, String nonce) {
		Matcher m = SCRIPT_TAG.matcher(html);
		StringBuffer out = new StringBuffer(html.length() + 64);
		while (m.find()) {
			String attributes = m.group(1);
			String replacement;
			if (HAS_SRC.matcher(attributes).find() || HAS_NONCE.matcher(attributes).find()) {
				replacement = m.group(0); // external, or already stamped
			} else {
				replacement = "<script nonce=\"" + nonce + "\"" + attributes + ">";
			}
			m.appendReplacement(out, Matcher.quoteReplacement(replacement));
		}
		m.appendTail(out);
		return out.toString();
	}

	private static final class CapturingResponse extends HttpServletResponseWrapper {

		private final HttpServletResponse delegate;
		private final long limit;

		private ByteArrayOutputStream buffer;
		private ServletOutputStream stream;
		private PrintWriter writer;
		private boolean passthrough;
		private boolean finished;

		CapturingResponse(HttpServletResponse delegate, long limit) {
			super(delegate);
			this.delegate = delegate;
			this.limit = limit;
		}

		private boolean buffering() {
			return this.buffer != null && !this.passthrough;
		}

		private void stopBuffering() throws IOException {
			if (this.passthrough) {
				return;
			}
			this.passthrough = true;
			if (this.buffer != null) {
				this.delegate.getOutputStream().write(this.buffer.toByteArray());
				this.buffer = null;
			}
		}

		@Override
		public void setHeader(String name, String value) {
			super.setHeader(name, value);
			encodingGuard(name);
		}

		@Override
		public void addHeader(String name, String value) {
			super.addHeader(name, value);
			encodingGuard(name);
		}

		private void encodingGuard(String name) {
			if (this.buffering() && "content-encoding".equalsIgnoreCase(name)) {
				try {
					this.stopBuffering();
				} catch (IOException e) {
					LOG.warn("Could not release the buffer after Content-Encoding was set: {}", e.getMessage());
				}
			}
		}

		@Override
		public void setContentType(String type) {
			super.setContentType(type);
			if (type != null && type.toLowerCase(Locale.ENGLISH).contains("text/html")
					&& this.buffer == null && this.stream == null && this.writer == null) {
				this.buffer = new ByteArrayOutputStream(64 * 1024);
			}
		}

		@Override
		public void setContentLength(int length) {
			if (!this.buffering()) {
				super.setContentLength(length);
			}
		}

		@Override
		public ServletOutputStream getOutputStream() throws IOException {
			if (!this.buffering()) {
				return super.getOutputStream();
			}
			if (this.stream == null) {
				this.stream = new ServletOutputStream() {
					@Override
					public void write(int b) throws IOException {
						CapturingResponse.this.write(b);
					}
				};
			}
			return this.stream;
		}

		@Override
		public PrintWriter getWriter() throws IOException {
			if (!this.buffering()) {
				return super.getWriter();
			}
			if (this.writer == null) {
				this.writer = new PrintWriter(
						new OutputStreamWriter(this.getOutputStream(), this.getCharacterEncoding()));
			}
			return this.writer;
		}

		private void write(int b) throws IOException {
			if (this.passthrough) {
				this.delegate.getOutputStream().write(b);
				return;
			}
			this.buffer.write(b);
			if (this.buffer.size() > this.limit) {
				LOG.warn("HTML exceeds the {} byte nonce-injection limit; serving it unmodified", this.limit);
				this.stopBuffering();
			}
		}

		@Override
		public void flushBuffer() throws IOException {
			if (!this.buffering()) {
				super.flushBuffer();
			}
		}

		void finish(String nonce) throws IOException {
			if (this.finished) {
				return;
			}
			this.finished = true;

			if (this.writer != null) {
				this.writer.flush();
			}
			if (this.buffer == null) {
				return;
			}

			byte[] captured = this.buffer.toByteArray();
			this.buffer = null;
			if (captured.length == 0) {
				return;
			}

			byte[] out = captured;
			if (nonce != null && !nonce.isEmpty()) {
				try {
					String encoding = this.getCharacterEncoding() != null ? this.getCharacterEncoding() : "UTF-8";
					out = addNonces(new String(captured, encoding), nonce).getBytes(encoding);
				} catch (Exception e) {
					LOG.warn("Nonce injection skipped: {}", e.getMessage());
					out = captured;
				}
			}
			this.delegate.setContentLength(out.length);
			this.delegate.getOutputStream().write(out);
			this.delegate.getOutputStream().flush();
		}
	}
}
