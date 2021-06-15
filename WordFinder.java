package Assignment7.PartI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public class WordFinder extends JFrame {

	JFileChooser jFileChooser;
	private JPanel topPanel; // the top line of objects is going to go here
	WordList wordList;
	JList list;
	private JTextArea wordsBox; // results of the search go in heer.
	String findText="";
	DefaultListModel<String> model;
	

	public WordFinder() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// set the size correctly
		this.setSize(500, 300);
		jFileChooser = new JFileChooser(".");
		wordList = new WordList();

		JPanel panelForTextFields = new JPanel();
		panelForTextFields.setSize(400, 180);

		//panel at the top
		createMenus();
		topPanel = new JPanel();

		// there should be objects in the top panel
		JLabel findLabel = new JLabel("Find: ");   //find label
		int FIELD_WIDTH = 15;
		JTextField findField = new JTextField(FIELD_WIDTH); //text field
		findField.setText("");
		JButton clearButton = new JButton("Clear");   //clear button

		// There should probably be something passed into the JScrollPane
		wordsBox = new JTextArea(
				10, 10);
		wordsBox.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(wordsBox);


		//findField key listener
		findField.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}
			@Override
			public void keyPressed(KeyEvent e) {
			}
			@Override
			public void keyReleased(KeyEvent e) {
				findText = findField.getText();
				find();
			}
		});
		//listener for clear button
		ActionListener listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				findField.setText("");
				findText = findField.getText();
				wordsBox.setCaretPosition(0);
			}
		};
		clearButton.addActionListener(listener);

		//JList with default model, did not use JList in this hw
		model = new DefaultListModel<>();
		list = new JList<>(model);

		// and of course you will want them to be properly aligned in the frame
		topPanel.add(findLabel);
		topPanel.add(findField);
		topPanel.add(clearButton);
		topPanel.setVisible(true);
		this.add(topPanel, BorderLayout.NORTH);
		this.add(scrollPane);
	}
	
	private void createMenus() {
		/* add a "File" menu with:
		 * "Open" item which allows you to choose a new file
		 * "Exit" item which ends the process with System.exit(0);
		 * Key shortcuts are optional
		 */
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);

		/* OpenActionListener that will open the file chooser */
		class OpenActionListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				OpenFileListener myFileListener = new OpenFileListener();
				myFileListener.actionPerformed(e);
			}
		}
		//add items(open and exit) into File menu
		JMenuItem open = createFileOpenItem();
		JMenuItem exit = createFileExitItem();
		fileMenu.add(open);
		fileMenu.add(exit);
	}

	//put the open and exit item here
	public JMenuItem createFileOpenItem()
	{
		JMenuItem item = new JMenuItem("Open");
		ActionListener listener = new OpenFileListener();
		item.addActionListener(listener);
		return item;
	}
	public JMenuItem createFileExitItem()
	{
		JMenuItem item = new JMenuItem("Exit");
		class MenuItemListener implements ActionListener
		{
			public void actionPerformed(ActionEvent event)
			{
				System.exit(0);
			}
		}
		ActionListener listener = new MenuItemListener();
		item.addActionListener(listener);
		return item;
	}
	//end

	private void find(){
		List searchResult = null;
		// figure out from WordList how to get this
		// you're going to want to get the words in the
		// searchResult list and put them in the textbox.
		wordsBox.setText("");
		if(findText.equals("")){
			for (Object i : wordList.getWords()) {
				wordsBox.append(i+"\n");
			}
		}else{
			searchResult = wordList.find(findText);
			for(Object i: searchResult){
				wordsBox.append(i+"\n");
			}
		}
		wordsBox.setCaretPosition(0);
	}
	
	class OpenFileListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int returnVal = jFileChooser.showOpenDialog(getParent());
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				try {
					//System.out.println("You chose to open this file: " + jFileChooser.getSelectedFile().getAbsolutePath());
					InputStream in = new FileInputStream(jFileChooser.getSelectedFile().getAbsolutePath());
					wordList.load(in);

					JScrollPane scrollPane = new JScrollPane(wordList);
					wordsBox.add(scrollPane);

					List searchResult = null;
					// figure out from WordList how to get this
					// because you will load in a wordlist and there
					// might be data in the search box
					find();
				} catch (IOException error){
					error.printStackTrace();
				}
			}
		}
	}


	public static void main(String[] args) {
		WordFinder wordFinder = new WordFinder();
		wordFinder.setVisible(true);
	}
}
