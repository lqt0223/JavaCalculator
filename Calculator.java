import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator{

	CalculatorData data = new CalculatorData();
	CalculatorController controller = new CalculatorController();
	CalculatorView view = new CalculatorView();

	//The constructor for initialization.
	public Calculator(){
		view.init();
	}

	// Data module
	class CalculatorData{
		String expression = "";
		ArrayList<Float> numbers = new ArrayList();
		ArrayList<String> operators = new ArrayList();
		void parse(){
			Pattern operatorPattern = Pattern.compile("[\\+\\-\\*/]");
			Matcher operatorMatcher = operatorPattern.matcher(expression);
			Pattern numberPattern = Pattern.compile("(\\d+\\.\\d+|\\d+)");
			Matcher numberMatcher = numberPattern.matcher(expression);
			while(operatorMatcher.find()){
				operators.add(operatorMatcher.group());
			}
			while(numberMatcher.find()){
				numbers.add(Float.parseFloat(numberMatcher.group()));
			}
		}
		void calculate(){
			this.parse();
			Float result = (Float)numbers.get(0);
			for(int i = 0; i < numbers.size() - 1 ; i++){
				switch(operators.get(i)){
					case "+": result += numbers.get(i+1);break;
					case "-": result -= numbers.get(i+1);break;
					case "*": result *= numbers.get(i+1);break;
					case "/": result /= numbers.get(i+1);break;
				}
			}
			view.displayOutput(result);
			clear();
			view.stopUpdatingTextfield();
		}
		void clear(){
			numbers.clear();
			operators.clear();
			expression = "";
		}

	// View module
	}
	class CalculatorView{
		ArrayList<JButton> buttons = new ArrayList();
		int mode = 0; //0 for appending new number, 1 for start receiving a new number.

		JFrame frame = new JFrame("Calculator");

		JPanel pan1 = new JPanel();
		JTextField textfield = new JTextField("",20);
		JButton buttonClear = new JButton("Clear");

		JPanel pan2 = new JPanel();
		JButton button0 = new JButton("0");
		JButton button1 = new JButton("1");
		JButton button2 = new JButton("2");
		JButton button3 = new JButton("3");
		JButton button4 = new JButton("4");
		JButton button5 = new JButton("5");
		JButton button6 = new JButton("6");
		JButton button7 = new JButton("7");
		JButton button8 = new JButton("8");
		JButton button9 = new JButton("9");
		JButton buttonDot = new JButton(".");
		JButton buttonPlus = new JButton("+");
		JButton buttonMinus = new JButton("-");
		JButton buttonMultiple = new JButton("*");
		JButton buttonDividedBy = new JButton("/");
		JButton buttonEquals = new JButton("=");

		void init(){
			buttons.add(button1);
			buttons.add(button2);
			buttons.add(button3);
			buttons.add(button4);
			buttons.add(button5);
			buttons.add(button6);
			buttons.add(button7);
			buttons.add(button8);
			buttons.add(button9);
			buttons.add(button0);
			buttons.add(buttonDot);
			buttons.add(buttonPlus);
			buttons.add(buttonMinus);
			buttons.add(buttonMultiple);
			buttons.add(buttonDividedBy);
			buttons.add(buttonEquals);	
			buttons.add(buttonClear);
			for(int i = 0 ;i < buttons.size();i++){
				JButton button = buttons.get(i);
				button.addActionListener(controller.new CalculatorActionListener());
				button.addKeyListener(controller.new CalculatorKeyListener());
			}

			//GridBag layout for the upper pane.
			GridBagLayout gridBagLayout = new GridBagLayout();
			pan1.setLayout(gridBagLayout);
			pan1.add(textfield);
			pan1.add(buttonClear);
			textfield.setEditable(false);
			textfield.setFocusable(false);
			buttonClear.requestFocus();
			textfield.setHorizontalAlignment(JTextField.RIGHT);
			textfield.setFont(new Font("Helvetica",0,70));

			GridBagConstraints constraints = new GridBagConstraints();
			constraints.fill = GridBagConstraints.BOTH;
			constraints.gridwidth = 1;
			constraints.weightx = 1;
			constraints.weighty = 0;
			gridBagLayout.setConstraints(textfield,constraints);
			constraints.gridwidth = 0;
			constraints.weightx = 0.1;
			constraints.weighty = 0;
			gridBagLayout.setConstraints(buttonClear,constraints);

			pan2.setLayout(new GridLayout(4,4,5,5));
			pan2.add(button7);
			pan2.add(button8);
			pan2.add(button9);
			pan2.add(buttonDividedBy);
			pan2.add(button4);
			pan2.add(button5);
			pan2.add(button6);
			pan2.add(buttonMultiple);
			pan2.add(button1);
			pan2.add(button2);
			pan2.add(button3);
			pan2.add(buttonMinus);
			pan2.add(button0);
			pan2.add(buttonDot);
			pan2.add(buttonEquals);
			pan2.add(buttonPlus);

			frame.setDefaultLookAndFeelDecorated(true);
			frame.setPreferredSize(new Dimension(450,400));
			frame.setResizable(false);
			frame.setLocation(300,300);
			frame.getContentPane().add(pan1,BorderLayout.NORTH);
			frame.getContentPane().add(pan2,BorderLayout.CENTER);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			frame.pack();
			frame.setVisible(true);
		}
		void displayOutput(Float result){
			String resultString = result.toString();
			if(resultString.substring(resultString.length() - 2).equals(".0")){  // Don't use operator "==" in Java to compare strings.
				resultString = resultString.substring(0,resultString.length() - 2);
			}
			textfield.setText(resultString);
		}
		void updateTextfield(String text){
			if(mode == 0){
				textfield.setText(textfield.getText() + text);
			}else{
				textfield.setText(text);
				mode = 0;
			}
		}
		void stopUpdatingTextfield(){
			mode = 1;
		}
		void clear(){
			textfield.setText("");
		}
		void toggleColor(int mode){
			if(mode == 1){
				textfield.setForeground(new Color(46,109,207));
			}else{
				textfield.setForeground(new Color(0,0,0));
			}
		}
	}

	// Controller module
	class CalculatorController{
		class CalculatorActionListener implements ActionListener{
			String text = "";
			public void actionPerformed(ActionEvent e){
				JButton button = (JButton)e.getSource();
				String buttonText = button.getText();
				switch(buttonText){
					case "=" : {
						data.calculate();
						view.toggleColor(1);
						break;
					}
					case "Clear" : {
						data.clear();
						view.clear();
						view.toggleColor(0);
						break;
					}
					default: {
						data.expression += buttonText;
						update(buttonText);
						view.toggleColor(0);
						break;
					}
				}
			}

			void update(String buttonText){
				switch(buttonText){
					case "+":
					case "-":
					case "*":
					case "/":{
						view.stopUpdatingTextfield();
						break;
					}
					default:{
						view.updateTextfield(buttonText);
						break;
					}
				}
			}
		}
		class CalculatorKeyListener implements KeyListener{
			public void keyPressed(KeyEvent e){
				
			}
			public void keyReleased(KeyEvent e){
				char keyChar = e.getKeyChar();
				for(int i = 0; i < view.buttons.size(); i++){
					JButton button = view.buttons.get(i);
					if(keyChar == button.getText().charAt(0)){
						button.doClick();
					}
				}
			}
			public void keyTyped(KeyEvent e){
			}
		}
	}
	
	// Main
	public static void main(String[] args) {
		Calculator calculator = new Calculator();
	}
}