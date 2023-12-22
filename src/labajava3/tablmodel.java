package labajava3;

import javax.swing.table.AbstractTableModel;

public class tablmodel extends AbstractTableModel {
	private Double[] coefficients;
	private Double from;
	private Double to;
	private Double step;
	public tablmodel(Double from, Double to, Double step,Double[] coefficients) {
		this.from = from;
		this.to = to;
		this.step = step;
		this.coefficients = coefficients;
	}
	public Double getFrom() {
		return from;
	}
	public Double getTo() {
		return to;
	}
	public Double getStep() {
		return step;
	}
	public int getColumnCount() {
		return 3;
	}
	@SuppressWarnings("removal")
	public int getRowCount() {
		return new Double(Math.ceil((to-from)/step)).intValue()+1;
	}
	public Object getValueAt(int row, int col) {
		double x = from + step * row; 

        Double result = 0.0;
        for (int i = coefficients.length - 1; i >= 0; i--) {
            result = result * x + coefficients[i];
        }
        boolean isWholeSquare;
        double wholePart = Math.floor(result);
        double squareRoot = Math.sqrt(wholePart);
        isWholeSquare = Math.floor(squareRoot) == squareRoot;


        switch(col) {
            case 0:
                return x;
            case 1:
                return result;
            case 2:
                return isWholeSquare;
        }
        return 0;
	}
	public String getColumnName(int col) {
		switch (col) {
		case 0:
			return "Значение X";
		case 1:
				return "Значение многочлена";
		case 2:
			return "Целая часть является квадратом?";
		}
		return null;
	}
	public Class<?> getColumnClass(int col) {
		if(col==0 || col==1) {
			return Double.class;
		}else {
			return Boolean.class;
		}
	}
	
	public Double[] getCoefficients() {
        return coefficients;
    }

    public void setCoefficients(Double[] coefficients) {
        this.coefficients = coefficients;
    }
}

