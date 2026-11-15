package com.varicent.reporting.web.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.imageio.ImageIO;

import org.jdom.Document;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactoryImp;
import com.varicent.common.pdf.PdfCell;
import com.varicent.common.pdf.PdfDocument;
import com.varicent.common.pdf.PdfImage;
import com.varicent.common.pdf.PdfLink;
import com.varicent.common.pdf.PdfTable;
import com.varicent.common.pdf.PdfText;
import com.varicent.common.ui.domain.CellFont;
import com.varicent.reporting.domain.Image;
import com.varicent.reporting.domain.WebReportReader;
import com.varicent.reporting.domain.ui.Border;
import com.varicent.reporting.domain.ui.Borders;
import com.varicent.reporting.domain.ui.ControlDisplayStyle;
import com.varicent.reporting.domain.ui.DataDisplayStyle;
import com.varicent.reporting.domain.ui.DataGridDisplayStyle;
import com.varicent.reporting.domain.ui.FixedGridDisplayStyle;
import com.varicent.reporting.domain.ui.Padding;
import com.varicent.reporting.domain.ui.ScalarDisplayStyle;
import com.varicent.reporting.domain.ui.WorkflowLinkDisplayStyle;

import sun.misc.BASE64Decoder;

public class PdfWebReportReader extends WebReportReader {
	private PdfDocument document;
	private Stack<Object> elementStack;
	private FontFactoryImp fontFactory;
	private HashMap<String, String> chartStreamMap;
	PdfCell headerBorderCell = new PdfCell();
	PdfCell headerBorderLastColumnCell = new PdfCell();
	PdfCell rowBorderCell = new PdfCell();
	PdfCell rowBorderLastColumnCell = new PdfCell();
	static final float defaultBorderSize = 0.5F;

	public PdfWebReportReader(Document xml, Map<Integer, Image> imageMap, String webAddress, PdfDocument document,
			HashMap<String, String> chartStreamM) {
		super(xml, imageMap, webAddress);
		this.document = document;
		this.elementStack = new Stack();
		this.chartStreamMap = chartStreamM;
		this.fontFactory = new FontFactoryImp();
		this.fontFactory.registerDirectories();
		this.headerBorderCell.getBorders().setAllColor(Color.BLACK);
		this.headerBorderCell.getBorders().setAllWidth(0.5F);
		this.headerBorderCell.getBorders().getRight().setWidth(0.0F);
		this.headerBorderLastColumnCell.getBorders().setAllColor(Color.BLACK);
		this.headerBorderLastColumnCell.getBorders().setAllWidth(0.5F);
		this.rowBorderCell.getBorders().setAllColor(Color.BLACK);
		this.rowBorderCell.getBorders().setAllWidth(0.5F);
		this.rowBorderCell.getBorders().getTop().setWidth(0.0F);
		this.rowBorderCell.getBorders().getRight().setWidth(0.0F);
		this.rowBorderLastColumnCell.getBorders().setAllColor(Color.BLACK);
		this.rowBorderLastColumnCell.getBorders().setAllWidth(0.5F);
		this.rowBorderLastColumnCell.getBorders().getTop().setWidth(0.0F);
	}

	private PdfCell getCurrentCell() {
		return (PdfCell) this.elementStack.peek();
	}

	private void startTable(PdfTable table) {
		this.elementStack.push(table);
	}

	private void finishTable(boolean showGridLines) {
		Stack<PdfCell> cells = new Stack();

		while (this.elementStack.peek() instanceof PdfCell) {
			cells.push((PdfCell) this.elementStack.pop());
		}

		PdfTable table = (PdfTable) this.elementStack.pop();
		int column = 0;
		int row = 0;
		boolean firstRow = true;
		boolean lastColumn = false;

		while (!cells.empty()) {
			PdfCell cell = cells.pop();
			lastColumn = column == table.getColumns() - 1;
			if (showGridLines) {
				this.setBorders(firstRow, lastColumn, cell);
			}

			table.add(column, row, 1, cell);
			++column;
			if (column >= table.getColumns()) {
				column = 0;
				++row;
				firstRow = false;
			}
		}

		if (this.elementStack.empty()) {
			this.document.add(table);
		} else {
			this.getCurrentCell().add(table);
		}

	}

	private void setBorders(boolean firstRow, boolean lastColumn, PdfCell cell) {
		if (firstRow && !lastColumn) {
			cell.setBorders(this.headerBorderCell.getBorders());
		} else if (firstRow && lastColumn) {
			cell.setBorders(this.headerBorderLastColumnCell.getBorders());
		} else if (!firstRow && !lastColumn) {
			cell.setBorders(this.rowBorderCell.getBorders());
		} else {
			cell.setBorders(this.rowBorderLastColumnCell.getBorders());
		}

	}

	private void addCell(PdfCell cell) {
		this.elementStack.push(cell);
	}

	@Override
	protected void beginLayout(int rows, float[] columnWidths) {
		this.startTable(new PdfTable(columnWidths, this.document.getPrintableHeight(), this.getPdfOptions()));
	}

	@Override
	protected void endLayout() {
		this.finishTable(false);
	}

	@Override
	protected void beginLayoutCell(Padding padding, Borders borders, WebReportReader.HorizontalAlignment horizontal,
			WebReportReader.VerticalAlignment vertical, Color background) {
		PdfCell cell = new PdfCell();
		this.applyStyle(cell, padding, borders);
		cell.setHorizontalAlign(this.toCellHorizontalAlignment(horizontal));
		cell.setVerticalAlign(this.toCellVerticalAlignment(vertical));
		cell.setBackground(background);
		this.addCell(cell);
	}

	@Override
	protected void beginDataGrid(int rows, float[] columnWidths, boolean containsHeader) {
		PdfTable table = new PdfTable(columnWidths, this.document.getPrintableHeight(), this.getPdfOptions());
		table.setHeaderRows(containsHeader ? 1 : 0);
		this.startTable(table);
	}

	@Override
	protected void endDataGrid(boolean showGridLines) {
		this.finishTable(showGridLines);
	}

	@Override
	protected void beginDataGridCell(Padding padding, Borders borders, WebReportReader.HorizontalAlignment horizontal,
			WebReportReader.VerticalAlignment vertical, Color background) {
		PdfCell cell = new PdfCell();
		this.applyStyle(cell, padding, borders);
		cell.setHorizontalAlign(this.toCellHorizontalAlignment(horizontal));
		cell.setVerticalAlign(this.toCellVerticalAlignment(vertical));
		cell.setBackground(background);
		this.addCell(cell);
	}

	@Override
	protected void beginFixedGrid(int rows, float[] columnWidths) {
		this.startTable(new PdfTable(columnWidths, this.document.getPrintableHeight(), this.getPdfOptions()));
	}

	@Override
	protected void endFixedGrid(boolean showGridLines) {
		this.finishTable(showGridLines);
	}

	@Override
	protected void beginFixedGridCell(Padding padding, Borders borders, WebReportReader.HorizontalAlignment horizontal,
			WebReportReader.VerticalAlignment vertical, Color background) {
		PdfCell cell = new PdfCell();
		this.applyStyle(cell, padding, borders);
		cell.setHorizontalAlign(this.toCellHorizontalAlignment(horizontal));
		cell.setVerticalAlign(this.toCellVerticalAlignment(vertical));
		cell.setBackground(background);
		this.addCell(cell);
	}

	@Override
	protected void addValue(Object value, Color color, CellFont font, String link, float maxWidth) {
		PdfText text = new PdfText(this.fontFactory);
		text.setText(value.toString());
		text.setFont(this.toiTextFont(font, color));
		if (maxWidth > 0.0F) {
			text.setMaxWidth(maxWidth);
		}

		if (link == null) {
			this.getCurrentCell().add(text);
		} else {
			PdfLink pdfLink = new PdfLink(this.fontFactory);
			pdfLink.setText(text);
			pdfLink.setUrl(link);
			this.getCurrentCell().add(pdfLink);
		}

	}

	@Override
	protected void addImage(Image image, String link) throws IOException {
		PdfImage pdfImage = new PdfImage(image.getBytes());
		if (link != null) {
			pdfImage.setLink(new PdfLink(this.fontFactory));
			pdfImage.getLink().setUrl(link);
		}

		this.getCurrentCell().add(pdfImage);
	}

	@Override
	protected void addFusionDisplay(String name, int width, int height) {
		if (this.chartStreamMap != null && this.chartStreamMap.containsKey(name)) {
			String data = this.chartStreamMap.get(name);
			byte[] imageBytes;
			if (data.split(";").length > 1) {
				imageBytes = getChartImage(data, width, height);
			} else {
				imageBytes = getChartImageBMP(data, width, height);
			}

			PdfImage pdfImage = new PdfImage(imageBytes);
			this.getCurrentCell().setUseBorderPadding(true);
			this.getCurrentCell().add(pdfImage);
		}

	}

	@Override
	protected void addControlValue(Object value, ControlDisplayStyle style) {
		if (value != null) {
			CellFont font;
			Color colour;
			if (style != null) {
				font = style.getStyle().getFont();
				colour = style.getStyle().getColor();
			} else {
				font = CellFont.defaultFont();
				colour = Color.BLACK;
				font.scaleSize(this.getPdfOptions().getCalculatedScaleValue());
			}

			PdfText text = new PdfText(this.fontFactory);
			text.setText(value.toString());
			text.setFont(this.toiTextFont(font, colour));
			this.getCurrentCell().add(text);
		}

	}

	private Font toiTextFont(CellFont cellFont, Color color) {
		int style = 0;
		if (cellFont.isBold()) {
			style |= 1;
		}

		if (cellFont.isItalic()) {
			style |= 2;
		}

		if (cellFont.isStruckout()) {
			style |= 8;
		}

		if (cellFont.isUnderlined()) {
			style |= 4;
		}

		BaseColor baseColor = new BaseColor(color.getRGB());
		Font font = this.fontFactory.getFont(cellFont.getFamily(), cellFont.getSize(), style, baseColor);
		font.setStyle(style);
		if (font.getBaseFont() == null && cellFont.isBold() && cellFont.isItalic()) {
			font = this.fontFactory.getFont(cellFont.getFamily(), cellFont.getSize(), 1, baseColor);
			if (font.getBaseFont() == null) {
				font = this.fontFactory.getFont(cellFont.getFamily(), cellFont.getSize(), 2, baseColor);
			}
		}

		return font;
	}

	@Override
	protected void ProcessPdfOptions() {
		if (this.getPdfOptions().isPortraitOrientation()) {
			this.document.setOrientation(PdfDocument.PageOrientation.Portrait);
		} else {
			this.document.setOrientation(PdfDocument.PageOrientation.Landscape);
		}

	}

	private void applyStyle(PdfCell cell, Padding padding, Borders borders) {
		this.setCellPadding(cell.getPadding(), padding);
		this.setCellBorder(cell.getBorders().getTop(), borders.getTop());
		this.setCellBorder(cell.getBorders().getBottom(), borders.getBottom());
		this.setCellBorder(cell.getBorders().getLeft(), borders.getLeft());
		this.setCellBorder(cell.getBorders().getRight(), borders.getRight());
	}

	private void setCellPadding(PdfCell.PdfCellPadding pdfPadding, Padding padding) {
		pdfPadding.setTop(padding.getTop());
		pdfPadding.setBottom(padding.getBottom());
		pdfPadding.setLeft(padding.getLeft());
		pdfPadding.setRight(padding.getRight());
	}

	private void setCellBorder(PdfCell.PdfCellBorder pdfBorder, Border border) {
		pdfBorder.setColor(border.getColor());
		pdfBorder.setWidth(border.getWidth());
	}

	private PdfCell.HorizontalAlignment toCellHorizontalAlignment(WebReportReader.HorizontalAlignment horizontal) {
		switch (horizontal) {
		case Center:
			return PdfCell.HorizontalAlignment.Center;
		case Justified:
			return PdfCell.HorizontalAlignment.Justify;
		case Left:
			return PdfCell.HorizontalAlignment.Left;
		case Right:
			return PdfCell.HorizontalAlignment.Right;
		default:
			return PdfCell.HorizontalAlignment.Left;
		}
	}

	private PdfCell.VerticalAlignment toCellVerticalAlignment(WebReportReader.VerticalAlignment vertical) {
		switch (vertical) {
		case Bottom:
			return PdfCell.VerticalAlignment.Bottom;
		case Middle:
			return PdfCell.VerticalAlignment.Middle;
		case Top:
			return PdfCell.VerticalAlignment.Top;
		default:
			return PdfCell.VerticalAlignment.Middle;
		}
	}

	@Override
	protected void scaleStyle(DataDisplayStyle style, float scale) {
		if (style instanceof DataGridDisplayStyle) {
			DataGridDisplayStyle dataGridStyle = (DataGridDisplayStyle) style;
			dataGridStyle.getHeaderStyle().getFont().scaleSize(scale);
			dataGridStyle.getEvenRowStyle().getFont().scaleSize(scale);
			dataGridStyle.getOddRowStyle().getFont().scaleSize(scale);
			dataGridStyle.getPadding().ScaleToSize(scale);
			dataGridStyle.getBorders().ScaleToSize(scale);
			if (dataGridStyle.getComputedRowStyle() != null) {
				dataGridStyle.getComputedRowStyle().getFont().scaleSize(scale);
			}
		} else if (style instanceof FixedGridDisplayStyle) {
			FixedGridDisplayStyle fixedGridStyle = (FixedGridDisplayStyle) style;
			fixedGridStyle.getCellStyle().getFont().scaleSize(scale);
			fixedGridStyle.getPadding().ScaleToSize(scale);
			fixedGridStyle.getBorders().ScaleToSize(scale);
		} else if (style instanceof ScalarDisplayStyle) {
			ScalarDisplayStyle scalarStyle = (ScalarDisplayStyle) style;
			scalarStyle.getStyle().getFont().scaleSize(scale);
			scalarStyle.getPadding().ScaleToSize(scale);
			scalarStyle.getBorders().ScaleToSize(scale);
		} else if (style instanceof WorkflowLinkDisplayStyle) {
			WorkflowLinkDisplayStyle workflowStyle = (WorkflowLinkDisplayStyle) style;
			workflowStyle.getPrimaryStyle().getFont().scaleSize(scale);
			workflowStyle.getSecondaryStyle().getFont().scaleSize(scale);
			workflowStyle.getHeaderStyle().getFont().scaleSize(scale);
		} else if (style instanceof ControlDisplayStyle) {
			ControlDisplayStyle controlStyle = (ControlDisplayStyle) style;
			controlStyle.getStyle().getFont().scaleSize(scale);
			controlStyle.getPadding().ScaleToSize(scale);
			controlStyle.getBorders().ScaleToSize(scale);
		}

	}

	@Override
	protected void scalePaddings(float scale) {
		this.dataGridPadding.setTop(this.dataGridPadding.getTop() * scale);
		this.dataGridPadding.setBottom(this.dataGridPadding.getBottom() * scale);
		this.dataGridPadding.setLeft(this.dataGridPadding.getLeft() * scale);
		this.dataGridPadding.setRight(this.dataGridPadding.getRight() * scale);
	}

	protected void setPageNumber() {
		this.document.setNumberPages(this.getPdfOptions().isIncludePageNumbers());
		this.document.setTotalPageNumber(this.getPdfOptions().isTotalPageNumber());
	}

	protected void setScaleValue() {
		this.document.setScaleValue(this.getPdfOptions().getCalculatedScaleValue());
	}

	protected void setFooterText() {
		this.document.setFooterText(this.getPdfOptions().getFooterText());
	}

	public static byte[] getChartImageBMP(String data, int width, int height) {
		BASE64Decoder decoder = new BASE64Decoder();

		try {
			byte[] decodedBytes = decoder.decodeBuffer(data);
			return decodedBytes;
		} catch (Exception var5) {
			return null;
		}
	}

	public static byte[] getChartImage(String data, int width, int height) {
		Color bgColor = new Color(Integer.parseInt("FFFFFF", 16));
		BufferedImage chart = null;

		try {
			String[] rows = new String[height + 1];
			rows = data.split(";");
			chart = new BufferedImage(width, height, 5);
			Graphics gr = chart.createGraphics();
			gr.setColor(bgColor);
			gr.fillRect(0, 0, width, height);
			int ri = 0;

			for (int i = 0; i < rows.length; ++i) {
				String[] pixels = rows[i].split(",");
				ri = 0;

				for (String pixel : pixels) {
					String[] clrs = pixel.split("_");
					String c = clrs[0];
					int r = Integer.parseInt(clrs[1]);
					if (c != null && c.length() > 0 && c != "") {
						if (c.length() < 6) {
							StringBuffer str = new StringBuffer(c);

							for (int p = c.length() + 1; p <= 6; ++p) {
								str.insert(0, "0");
							}

							c = str.toString();
						}

						for (int k = 1; k <= r; ++k) {
							gr.setColor(new Color(Integer.parseInt(c, 16)));
							gr.fillRect(ri, i, 1, 1);
							++ri;
						}
					} else {
						ri += r;
					}
				}
			}
		} catch (Exception var17) {
			return null;
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {
			ImageIO.write(chart, "png", baos);
			baos.flush();
			return baos.toByteArray();
		} catch (Exception var16) {
			return null;
		}
	}
}
