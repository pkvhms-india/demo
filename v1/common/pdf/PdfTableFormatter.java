package com.varicent.common.pdf;

import java.util.ArrayList;
import java.util.List;

public final class PdfTableFormatter {
	public static void resizeColumnsToFit(PdfTable table, float absoluteTotalWidth) {
		float[] minAbsoluteColumnWidths = table.getMinimumColumnWidths();
		float[] relativeColumnWidths = table.getColumnWidths();
		float totalRelativeColumnWidth = 0.0F;

		for (int i = 0; i < table.getColumns(); ++i) {
			totalRelativeColumnWidth += relativeColumnWidths[i];
		}

		PdfColumn[] columns = new PdfColumn[table.getColumns()];
		List<PdfColumn> availColumns = new ArrayList();

		for (int i = 0; i < table.getColumns(); ++i) {
			columns[i] = new PdfColumn(minAbsoluteColumnWidths[i],
					relativeColumnWidths[i] / totalRelativeColumnWidth * absoluteTotalWidth, relativeColumnWidths[i]);
			availColumns.add(columns[i]);
		}

		while (true) {
			PdfColumn column = null;

			for (PdfColumn c : availColumns) {
				if (c.isSquished()) {
					column = c;
					break;
				}
			}

			if (column == null) {
				float[] columnWidths = new float[columns.length];

				for (int i = 0; i < columns.length; ++i) {
					columnWidths[i] = columns[i].getAbsoluteWidth();
				}

				table.setColumnWidths(columnWidths);

				for (PdfTable.PdfTableCell cell : table.getTableCells()) {
					for (PdfElement element : cell.getCell().getElements()) {
						if (element instanceof PdfTable) {
							resizeColumnsToFit((PdfTable) element, columnWidths[cell.getX()]);
						}
					}
				}

				return;
			}

			availColumns.remove(column);
			float difference = column.getMinAbsoluteWidth() - column.getAbsoluteWidth();
			float relativeTotal = 0.0F;

			for (PdfColumn c : availColumns) {
				relativeTotal += c.getRelativeWidth();
			}

			for (PdfColumn c : availColumns) {
				c.setAbsoluteWidth(c.getAbsoluteWidth() - c.getRelativeWidth() / relativeTotal * difference);
			}

			column.setAbsoluteWidth(column.getMinAbsoluteWidth());
		}
	}

	private static class PdfColumn {
		private float minAbsoluteWidth;
		private float absoluteWidth;
		private float relativeWidth;

		public PdfColumn(float minAbsoluteWidth, float absoluteWidth, float relativeWidth) {
			this.minAbsoluteWidth = minAbsoluteWidth;
			this.absoluteWidth = absoluteWidth;
			this.relativeWidth = relativeWidth;
		}

		public float getMinAbsoluteWidth() {
			return this.minAbsoluteWidth;
		}

		public float getAbsoluteWidth() {
			return this.absoluteWidth;
		}

		public void setAbsoluteWidth(float absoluteWidth) {
			this.absoluteWidth = absoluteWidth;
		}

		public float getRelativeWidth() {
			return this.relativeWidth;
		}

		public boolean isSquished() {
			return this.absoluteWidth < this.minAbsoluteWidth;
		}
	}
}
