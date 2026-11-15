package com.varicent.common.pdf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.springframework.util.Assert;

import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.varicent.reporting.domain.WebReportPdfOptions;

public class PdfTable implements PdfElement {
	private List<PdfTableCell> cells;
	private int headerRows;
	private PdfCell defaultCell;
	private float[] columnWidths;
	private float[] minColumnWidths;
	private int rows;
	private int minRows;
	private float maxRowHeight;
	private WebReportPdfOptions pdfOption;

	public PdfTable(float[] columnWidths, float maxRowHeight, WebReportPdfOptions pdfOption) {
		Assert.state(columnWidths.length >= 0, "Table must have at least 1 column");
		this.cells = new ArrayList();
		this.headerRows = 0;
		this.defaultCell = new PdfCell();
		this.columnWidths = columnWidths;
		this.minColumnWidths = new float[columnWidths.length];
		this.rows = this.minRows = 0;
		this.maxRowHeight = maxRowHeight;
		this.pdfOption = pdfOption;
	}

	public int getHeaderRows() {
		return this.headerRows;
	}

	public void setHeaderRows(int headerRows) {
		this.headerRows = Math.max(0, headerRows);
	}

	public PdfCell getDefaultCell() {
		return this.defaultCell;
	}

	public void setDefaultCell(PdfCell defaultCell) {
		this.defaultCell = defaultCell;
	}

	public int getColumns() {
		return this.columnWidths.length;
	}

	public int getRows() {
		return Math.max(this.rows, this.minRows);
	}

	public void setRows(int rows) {
		this.rows = Math.max(this.minRows, rows);
	}

	public float[] getColumnWidths() {
		return this.columnWidths;
	}

	public void setColumnWidths(float[] columnWidths) {
		Assert.state(this.columnWidths.length == columnWidths.length, "Column lengths must be the same.");
		this.columnWidths = columnWidths;
	}

	public PdfCell[] getCells() {
		PdfCell[] ret = new PdfCell[this.cells.size()];

		for (int i = 0; i < this.cells.size(); ++i) {
			ret[i] = this.cells.get(i).getCell();
		}

		return ret;
	}

	public List<PdfTableCell> getTableCells() {
		return this.cells;
	}

	public void add(int x, int y, int colSpan, PdfCell cell) {
		PdfTableCell tableCell = new PdfTableCell(x, y, colSpan, cell);
		this.cells.add(tableCell);
		this.minColumnWidths[x] = Math.max(this.minColumnWidths[x], cell.calculateMinimumWidth());
		this.minRows = Math.max(this.minRows, tableCell.getY2() + 1);
	}

	@Override
	public float calculateMinimumWidth() {
		float minWidth = 0.0F;

		for (float width : this.getMinimumColumnWidths()) {
			minWidth += width;
		}

		return minWidth;
	}

	@Override
	public float calculateMinimumHeight() {
		float max = 0.0F;

		for (PdfTableCell cell : this.cells) {
			max = Math.max(max, cell.cell.calculateMinimumHeight());
		}

		return max;
	}

	public float[] getMinimumColumnWidths() {
		return this.minColumnWidths;
	}

	@Override
	public Element[] toElements() {
		Collections.sort(this.cells);
		int columnCount = this.getColumns();
		int rowCount = this.getRows();
		if (columnCount >= 1 && rowCount >= 1) {
			PdfPTable table = new PdfPTable(this.columnWidths);
			table.setWidthPercentage(100.0F);
			table.setSplitRows(true);
			table.setSplitLate(false);
			table.setHeaderRows(this.headerRows);
			Queue<PdfTableCell> cellQueue = new LinkedList(this.cells);
			PdfTableCell next = cellQueue.size() > 0 ? (PdfTableCell) cellQueue.poll() : null;

			for (int row = 0; row < rowCount; ++row) {
				int column = 0;

				while (column < columnCount) {
					if (next != null && next.getY() == row && next.getX() == column) {
						PdfCell cell = next.cell;
						if (cell.getElements().size() > 0 && cell.getElements().get(0) instanceof PdfImage) {
							PdfImage image = (PdfImage) cell.getElements().get(0);
							float initialScale = this.pdfOption.getCalculatedScaleValue();
							image.scaleImage(initialScale);
							PdfCell.PdfCellPadding padding = cell.getPadding();
							PdfImage.Size scaledSize = image.getScaledDimension();
							float totalWidth = scaledSize.getWidth() + padding.getLeft() + padding.getRight();
							float totalHeight = scaledSize.getHeight() + padding.getTop() + padding.getBottom();
							boolean useBorderPadding = cell.getUseBorderPadding();
							if (useBorderPadding) {
								PdfCell.PdfCellBorders borders = cell.getBorders();
								totalWidth += borders.getLeft().getWidth() + borders.getRight().getWidth();
								totalHeight += borders.getTop().getWidth() + borders.getBottom().getWidth();
							}

							float columnWidth = this.columnWidths[column];
							if (totalWidth > columnWidth || totalHeight > this.maxRowHeight) {
								float scaleValue = WebReportPdfOptions.getCalculatedScaleValue(totalWidth, totalHeight,
										columnWidth - 1.0F, this.maxRowHeight - 1.0F);
								image.scaleImage(scaleValue);
								if (scaleValue < 1.0F && scaleValue > 0.0F) {
									padding.setTop(padding.getTop() * scaleValue);
									padding.setBottom(padding.getBottom() * scaleValue);
									padding.setRight(padding.getRight() * scaleValue);
									padding.setLeft(padding.getLeft() * scaleValue);
									if (useBorderPadding) {
										PdfCell.PdfCellBorders borders = cell.getBorders();
										borders.getTop().setWidth(borders.getTop().getWidth() * scaleValue);
										borders.getBottom().setWidth(borders.getBottom().getWidth() * scaleValue);
										borders.getRight().setWidth(borders.getRight().getWidth() * scaleValue);
										borders.getLeft().setWidth(borders.getLeft().getWidth() * scaleValue);
									}
								}
							}
						}

						table.addCell(next.ToElement());
						column += next.getColumnSpan();
						next = cellQueue.size() > 0 ? (PdfTableCell) cellQueue.poll() : null;
					} else {
						PdfPCell cell = this.defaultCell.toElement();
						cell.setColspan(1);
						table.addCell(cell);
						++column;
					}
				}
			}

			return new Element[] { table };
		} else {
			return new Element[0];
		}
	}

	public List<PdfCell> getCellsInColumn(int column) {
		List<PdfCell> cellList = new ArrayList();

		for (PdfTableCell cell : this.cells) {
			if (cell.intersectsRectangle(column, column, 0, this.getRows())) {
				cellList.add(cell.getCell());
			}
		}

		return cellList;
	}

	public class PdfTableCell implements Comparable<PdfTableCell> {
		private int x;
		private int y;
		private int columnSpan;
		private PdfCell cell;

		public PdfTableCell(int x, int y, int columnSpan, PdfCell cell) {
			this.x = Math.max(0, x);
			this.y = Math.max(0, y);
			this.columnSpan = Math.max(1, columnSpan);
			this.cell = cell;
		}

		public int getX() {
			return this.x;
		}

		public int getY() {
			return this.y;
		}

		public int getX2() {
			return this.x + this.columnSpan - 1;
		}

		public int getY2() {
			return this.y;
		}

		public int getColumnSpan() {
			return this.columnSpan;
		}

		public PdfCell getCell() {
			return this.cell;
		}

		public boolean intersectsCell(PdfTableCell cell) {
			return this.intersectsRectangle(cell.getX(), cell.getX2(), cell.getY(), cell.getY2());
		}

		public boolean intersectsRectangle(int rX, int rX2, int rY, int rY2) {
			return this.getX() <= rX2 && this.getX2() >= rX && this.getY() <= rY2 && this.getY2() >= rY;
		}

		public PdfPCell ToElement() {
			PdfPCell c = this.cell.toElement();
			c.setColspan(this.columnSpan);
			return c;
		}

		@Override
		public int compareTo(PdfTableCell o) {
			if (this.getY() != o.getY()) {
				return this.getY() < o.getY() ? -1 : 1;
			} else {
				return this.getX() < o.getX() ? -1 : 1;
			}
		}
	}
}
