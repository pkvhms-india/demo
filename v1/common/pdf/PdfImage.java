package com.varicent.common.pdf;

import java.io.IOException;
import java.net.MalformedURLException;

import com.itextpdf.text.Annotation;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;

public class PdfImage implements PdfElement {
	private Image image;
	private byte[] bytes;
	private PdfLink link;
	private float scalePercent;

	public PdfImage(byte[] bytes) {
		this(bytes, (PdfLink) null);
	}

	public PdfImage(byte[] bytes, PdfLink link) {
		this.scalePercent = 1.0F;
		this.image = null;
		this.bytes = bytes;
		this.link = link;
	}

	public PdfLink getLink() {
		return this.link;
	}

	public void setLink(PdfLink link) {
		this.link = link;
	}

	public byte[] getBytes() {
		return this.bytes;
	}

	@Override
	public float calculateMinimumWidth() {
		Image image = this.getImage();
		return image.getScaledWidth() / 96.0F * 72.0F;
	}

	@Override
	public float calculateMinimumHeight() {
		Image image = this.getImage();
		return image.getScaledHeight() / 96.0F * 72.0F;
	}

	public Image toElement() {
		return this.getImage();
	}

	@Override
	public Element[] toElements() {
		return new Element[] { this.toElement() };
	}

	private Image getImage() {
		if (this.image != null) {
			return this.image;
		} else {
			try {
				this.image = Image.getInstance(this.bytes);
			} catch (MalformedURLException e) {
				throw new RuntimeException(e);
			} catch (BadElementException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			if (this.link != null) {
				this.image.setAnnotation(new Annotation(0.0F, 0.0F, 0.0F, 0.0F, this.link.getUrl()));
			}

			return this.image;
		}
	}

	public void scaleImage(float scaleValue) {
		if (scaleValue < 1.0F && scaleValue > 0.0F) {
			this.scalePercent *= scaleValue;
			this.getImage();
			this.image.scalePercent(this.scalePercent * 100.0F);
		}

	}

	public Size getScaledDimension() {
		this.getImage();
		return new Size(this.image.getWidth(), this.image.getHeight());
	}

	public float getScaledHeight() {
		this.getImage();
		return this.image.getScaledHeight();
	}

	public class Size {
		float width;
		float height;

		public Size(float width, float height) {
			this.width = width;
			this.height = height;
		}

		public float getWidth() {
			return this.width;
		}

		public void setWidth(float width) {
			this.width = width;
		}

		public float getHeight() {
			return this.height;
		}

		public void setHeight(float height) {
			this.height = height;
		}
	}
}
