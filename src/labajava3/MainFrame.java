package labajava3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class MainFrame extends JFrame{
	private static final int WIDTH = 700;
	private static final int HEIGHT = 500;
	private Double[] coefficients;
	
	
	private JFileChooser fileChooser = null;
	
	private JMenuItem saveToTextMenuItem;
	private JMenuItem saveToGraphicsMenuItem;
	private JMenuItem searchValueMenuItem;
	private JMenuItem infoMenuItem;
	// Поля ввода для считывания значений переменных
	private JTextField textFieldFrom;
	private JTextField textFieldTo;
	private JTextField textFieldStep;
	private Box hBoxResult;
	// Визуализатор ячеек таблицы
	private GornerTableCellRenderer renderer = new GornerTableCellRenderer();
	// Модель данных с результатами вычислений
	private tablmodel data;
	
	public void createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu fileMenu = new JMenu("Файл");
		menuBar.add(fileMenu);
		
		JMenu tableMenu = new JMenu("Таблица");
		menuBar.add(tableMenu);
		
		JMenu referenceMenu = new JMenu("Справка");
		menuBar.add(referenceMenu);	
		
		AbstractAction infoAction = new AbstractAction("О программе") {
			public void actionPerformed(ActionEvent event) {
				String surname = "Богуш";
                String group = "6";
                
                JPanel panel = new JPanel();
                panel.setLayout(new BorderLayout());
                panel.add(new JLabel("Фамилия: " + surname), BorderLayout.NORTH);
                panel.add(new JLabel("Группа: " + group), BorderLayout.SOUTH);

                JOptionPane.showMessageDialog(null, panel, "О программе", JOptionPane.PLAIN_MESSAGE);
			}
		};
		infoMenuItem = referenceMenu.add(infoAction);
		
		
		Action saveToTextAction = new AbstractAction("Сохранить втекстовый файл") {
			public void actionPerformed(ActionEvent event) {
				if (fileChooser==null) {
					fileChooser = new JFileChooser();
					fileChooser.setCurrentDirectory(new File("."));
				}
				if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION)
					saveToTextFile(fileChooser.getSelectedFile());
				}
		};
		
		saveToTextMenuItem = fileMenu.add(saveToTextAction);
		saveToTextMenuItem.setEnabled(false);
		
		
		Action saveToGraphicsAction = new AbstractAction("Сохранить данные для построения графика") {
			public void actionPerformed(ActionEvent event) {
				if (fileChooser==null) {
					fileChooser = new JFileChooser();
					fileChooser.setCurrentDirectory(new File("."));
				}
				if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION);
					saveToGraphicsFile(
					fileChooser.getSelectedFile());
				}
		};
		
		saveToGraphicsMenuItem = fileMenu.add(saveToGraphicsAction);
		saveToGraphicsMenuItem.setEnabled(false);

		
		Action searchValueAction = new AbstractAction("Найти значение многочлена") {
			public void actionPerformed(ActionEvent event) {
				String value = JOptionPane.showInputDialog(MainFrame.this, "Введите значение для поиска", "Поиск значения", JOptionPane.QUESTION_MESSAGE);
				renderer.setNeedle(value);
				getContentPane().repaint();
			}
		};
		searchValueMenuItem = tableMenu.add(searchValueAction);
		searchValueMenuItem.setEnabled(false);
		
	}
	public void inputsField() {
		JLabel labelForFrom = new JLabel("X изменяется на интервале от:");
		// Создать текстовое поле для ввода значения длиной в 10 символов
		// со значением по умолчанию 0.0
		textFieldFrom = new JTextField("0.0", 10);
		// Установить максимальный размер равный предпочтительному, чтобы
		// предотвратить увеличение размера поля ввода
		textFieldFrom.setMaximumSize(textFieldFrom.getPreferredSize());
		// Создать подпись для ввода левой границы отрезка
		JLabel labelForTo = new JLabel("до:");
		// Создать текстовое поле для ввода значения длиной в 10 символов
		// со значением по умолчанию 1.0
		textFieldTo = new JTextField("1.0", 10);
		// Установить максимальный размер равный предпочтительному, чтобы
		// предотвратить увеличение размера поля ввода
		textFieldTo.setMaximumSize(textFieldTo.getPreferredSize());
		// Создать подпись для ввода шага табулирования
		JLabel labelForStep = new JLabel("с шагом:");
		// Создать текстовое поле для ввода значения длиной в 10 символов
		// со значением по умолчанию 1.0
		textFieldStep = new JTextField("0.1", 10);
		// Установить максимальный размер равный предпочтительному, чтобы
		// предотвратить увеличение размера поля ввода
		textFieldStep.setMaximumSize(textFieldStep.getPreferredSize());
		
		createContainerLabel(labelForFrom,labelForTo,labelForStep);

	}
	public void createContainerLabel(JLabel labelForFrom,JLabel labelForTo,JLabel labelForStep) {
		// Создать контейнер 1 типа "коробка с горизонтальной укладкой"
		Box hboxRange = Box.createHorizontalBox();
		// Задать для контейнера тип рамки "объѐмная"
		hboxRange.setBorder(BorderFactory.createBevelBorder(1));
		// Добавить "клей" C1-H1
		hboxRange.add(Box.createHorizontalGlue());
		// Добавить подпись "От"
		hboxRange.add(labelForFrom);
		// Добавить "распорку" C1-H2
		hboxRange.add(Box.createHorizontalStrut(10));
		// Добавить поле ввода "От"
		hboxRange.add(textFieldFrom);
		// Добавить "распорку" C1-H3
		hboxRange.add(Box.createHorizontalStrut(20));
		// Добавить подпись "До"
		hboxRange.add(labelForTo);
		// Добавить "распорку" C1-H4
		hboxRange.add(Box.createHorizontalStrut(10));
		// Добавить поле ввода "До"
		hboxRange.add(textFieldTo);
		// Добавить "распорку" C1-H5
		hboxRange.add(Box.createHorizontalStrut(20));
		// Добавить подпись "с шагом"
		hboxRange.add(labelForStep);
		// Добавить "распорку" C1-H6
		hboxRange.add(Box.createHorizontalStrut(10));
		// Добавить поле для ввода шага табулирования
		hboxRange.add(textFieldStep);
		// Добавить "клей" C1-H7
		hboxRange.add(Box.createHorizontalGlue());
		// Установить предпочтительный размер области равным удвоенному
		// минимальному, чтобы при компоновке область совсем не сдавили
	
		new Double(hboxRange.getMaximumSize().getWidth()).intValue();
		new Double(hboxRange.getMinimumSize().getHeight()).intValue();//////////////////////////////////////////////////////////////////////////////////
		// Установить область в верхнюю (северную) часть компоновки
		getContentPane().add(hboxRange, BorderLayout.NORTH);
		// Создать кнопку "Вычислить"
	}
	
	
	public void ButtunLayout(JButton buttonCalc, JButton buttonReset) {
		Box hboxButtons = Box.createHorizontalBox();
		hboxButtons.setBorder(BorderFactory.createBevelBorder(1));
		hboxButtons.add(Box.createHorizontalGlue());
		hboxButtons.add(buttonCalc);
		hboxButtons.add(Box.createHorizontalStrut(30));
		hboxButtons.add(buttonReset);
		hboxButtons.add(Box.createHorizontalGlue());
		// Установить предпочтительный размер области равным удвоенному
		//минимальному, чтобы при
		// компоновке окна область совсем не сдавили
		hboxButtons.setPreferredSize(
				new Dimension(new Double(hboxButtons.getMaximumSize().getWidth()).intValue(), 
				new Double(hboxButtons.getMinimumSize().getHeight()).intValue()*2)
				);
		// Разместить контейнер с кнопками в нижней (южной) области
		//граничной компоновки
		getContentPane().add(hboxButtons, BorderLayout.SOUTH);
		// Область для вывода результата пока что пустая
		hBoxResult = Box.createHorizontalBox();
		// Создать область с полями ввода для границ отрезка и шага
		// Создать подпись для ввода левой границы отрезка
		hBoxResult.add(new JPanel());
		// Установить контейнер hBoxResult в главной (центральной) области
		//граничной компоновки
		getContentPane().add(hBoxResult, BorderLayout.CENTER);
	}
	
	
	public void createButtun() {
		JButton buttonCalc = new JButton("Вычислить");
		// Задать действие на нажатие "Вычислить" и привязать к кнопке
		
	/////////////////////////////////////////////////////////////////////////////////////////////////////создание решения в таблице
		buttonCalc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				try {
				// Считать значения начала и конца отрезка, шага
					Double from = Double.parseDouble(textFieldFrom.getText());
					Double to = Double.parseDouble(textFieldTo.getText());
					Double step = Double.parseDouble(textFieldStep.getText());
					// На основе считанных данных создать новый
					//экземпляр модели таблицы
					data = new tablmodel(from, to, step,MainFrame.this.coefficients);
					// Создать новый экземпляр таблицы
					JTable table = new JTable(data);
					// Установить в качестве визуализатора ячеек для
					//класса Double разработанный визуализатор
					table.setDefaultRenderer(Double.class,renderer);
					// Установить размер строки таблицы в 30
					//пикселов
					table.setRowHeight(30);
					// Удалить все вложенные элементы из контейнера hBoxResult
					hBoxResult.removeAll();
					// Добавить в hBoxResult таблицу, "обѐрнутую" в
					//панель с полосами прокрутки
					hBoxResult.add(new JScrollPane(table));
					// Обновить область содержания главного окна
					getContentPane().validate();
					// Пометить ряд элементов меню как доступных
					saveToTextMenuItem.setEnabled(true);
					saveToGraphicsMenuItem.setEnabled(true);
					searchValueMenuItem.setEnabled(true);
				} catch (NumberFormatException ex) {
				// В случае ошибки преобразования чисел показать
				//сообщение об ошибке
					JOptionPane.showMessageDialog(MainFrame.this,
					"Ошибка в формате записи числа с плавающей точкой", "Ошибочный формат числа",
					JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		JButton buttonReset = new JButton("Очистить поля");
		buttonReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				// Установить в полях ввода значения по умолчанию
				textFieldFrom.setText("0.0");
				textFieldTo.setText("1.0");
				textFieldStep.setText("0.1");
				// Удалить все вложенные элементы контейнера hBoxResult
				hBoxResult.removeAll();
				// Добавить в контейнер пустую панель
				hBoxResult.add(new JPanel());
				// Пометить элементы меню как недоступные
				saveToTextMenuItem.setEnabled(false);
				saveToGraphicsMenuItem.setEnabled(false);
				searchValueMenuItem.setEnabled(false);
				// Обновить область содержания главного окна
				getContentPane().validate();
			}
		});
		
		ButtunLayout(buttonCalc,buttonReset);
	}
	
	public MainFrame(Double[] coefficients) {
		// Обязательный вызов конструктора предка
		super("Табулирование многочлена на отрезке по схеме Горнера");
		this.coefficients = coefficients;
		setSize(WIDTH, HEIGHT);
		Toolkit kit = Toolkit.getDefaultToolkit();
		setLocation((kit.getScreenSize().width - WIDTH)/2, (kit.getScreenSize().height - HEIGHT)/2);
		
		createMenuBar();
		inputsField();
		createButtun();
		
	}
	
	protected void saveToGraphicsFile(File selectedFile) {
		try {
			// Создать новый байтовый поток вывода, направленный в
			//указанный файл
			DataOutputStream out = new DataOutputStream(new
			FileOutputStream(selectedFile));
			// Записать в поток вывода попарно значение X в точке,
			//значение многочлена в точке
			for (int i = 0; i<data.getRowCount(); i++) {
				out.writeDouble((Double)data.getValueAt(i,0));
				out.writeDouble((Double)data.getValueAt(i,1));
			}
			// Закрыть поток вывода
			out.close();
		} catch (Exception e) {
		// Исключительную ситуацию "ФайлНеНайден" в данном случае
		//можно не обрабатывать,
		// так как мы файл создаѐм, а не открываем для чтения
		}
	}
	protected void saveToTextFile(File selectedFile) {
		try {
			// Создать новый символьный поток вывода, направленный в
			//указанный файл
			PrintStream out = new PrintStream(selectedFile);
			// Записать в поток вывода заголовочные сведения
			out.println("Результаты табулирования многочлена по схеме Горнера");
			out.print("Многочлен: ");
			for (int i=0; i<coefficients.length; i++) {
				out.print(coefficients[i] + "*X^" + (coefficients.length-i-1));
				if (i!=coefficients.length-1)
					out.print(" + ");
			}
			out.println("");
			out.println("Интервал от " + data.getFrom() + " до " +
			data.getTo() + " с шагом " + data.getStep());
			out.println("====================================================");
			// Записать в поток вывода значения в точках
			for (int i = 0; i<data.getRowCount(); i++) {
				out.println("Значение в точке " + data.getValueAt(i,0) + " равно " + data.getValueAt(i,1));
			}
			// Закрыть поток
			out.close();
			} catch (FileNotFoundException e) {
			// Исключительную ситуацию "ФайлНеНайден" можно не
			// обрабатывать, так как мы файл создаѐм, а не открываем
			}
	}
}

