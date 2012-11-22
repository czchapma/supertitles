package supertitles;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

public class SetupPanel{
	private Supertitles _st;
	private JPanel _panel;
	private JLabel _instructions, _filepath, _forwardLabel,_backLabel;
	private JTextField _numCharacters, _fontSize;
	private JButton _open,_start, _forwardButton, _backButton;
	private KeyEvent _forwardOption, _backOption;
	private RecordingState _currentState;
	private String _fileName;
	private enum RecordingState{
		ExpectForward,ExpectBackward,None
	}
	private class ParseException extends Exception{
		public ParseException(){}
	}
	
	private class SetupButton extends JButton{
		public SetupButton(String text){
			super(text);
			setAlignmentX(JButton.CENTER_ALIGNMENT);
			setFont(new Font("Sans Seriff",Font.PLAIN,20));
			_panel.add(this);
		}
	}
	
	public SetupPanel(Supertitles s){
		_st = s;
		_currentState = RecordingState.None;
		_panel = new JPanel();
		_panel.setVisible(true);
		_panel.setBackground(Color.GRAY);
		_panel.setLayout(new BoxLayout(_panel,BoxLayout.Y_AXIS));
		_panel.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent e) {
				//Record Forward Key
				if(_currentState == RecordingState.ExpectForward){
					_forwardOption = e;
					_forwardButton.setText("Change forward option");
					_forwardLabel.setText("Forward: " + KeyEvent.getKeyText(e.getKeyCode()));
					_st.getPlayPanel().resetValues(e.getKeyCode(), true);
					
					//Record Backward Key
				} else if(_currentState == RecordingState.ExpectBackward){
					_backOption = e;
					_backButton.setText("Change backward option");
					_backLabel.setText("Backward: " + KeyEvent.getKeyText(e.getKeyCode()));
					_st.getPlayPanel().resetValues(e.getKeyCode(),false);
				}
				
				_currentState = RecordingState.None;
				_panel.repaint();
			}

			@Override
			public void keyReleased(KeyEvent arg0) {}
			@Override
			public void keyTyped(KeyEvent arg0) {}
			
		});
		
		//Instructions label
		_instructions = new JLabel("Welcome to the supertiles program!");
		_instructions.setFont(new Font("Sans Seriff",Font.BOLD,35));
		_instructions.setForeground(Color.cyan);
		_instructions.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		_panel.add(_instructions);

		//File Label 
		_filepath = new JLabel("Select a txt file with one supertitle line per file line, use a ^ to denote two combined lines");
		_filepath.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		_panel.add(_filepath);
		
		//Open File Button
		_open = new SetupButton("Open File");
		_open.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				int returnValue = fc.showOpenDialog(_panel);
				if(returnValue == JFileChooser.APPROVE_OPTION){
					File file = fc.getSelectedFile();
					_fileName = file.getPath();
					_filepath.setText(file.getPath());
				}
				_panel.requestFocusInWindow();

			}
		});

		//Field to set number of buttons
		_numCharacters = new JTextField("Max characters per line");
		_numCharacters.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {
				if(_numCharacters.getText().equals(""))
					_numCharacters.setText("Max characters per line");
			}

			@Override
			public void mousePressed(MouseEvent e) {
				_numCharacters.setText("");
			}

			@Override
			public void mouseReleased(MouseEvent e) {}
			
		});
		_numCharacters.setFont(new Font(_numCharacters.getFont().getName(),_numCharacters.getFont().getStyle(),20));
		_numCharacters.setMaximumSize(new Dimension(300,100));
		_numCharacters.setAlignmentX(Component.CENTER_ALIGNMENT);
		_numCharacters.setHorizontalAlignment(JTextField.CENTER);
		_panel.add(_numCharacters);
		
		//Field to change font size
		_fontSize = new JTextField("Enter Font Size");
		_fontSize.setMaximumSize(new Dimension(300,100));
		_fontSize.setAlignmentX(Component.CENTER_ALIGNMENT);
		_fontSize.setHorizontalAlignment(JTextField.CENTER);
		_fontSize.setFont(new Font(_fontSize.getFont().getName(),_fontSize.getFont().getStyle(),20));
		_fontSize.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {
				//Never leave text box empty
				if(_fontSize.getText().equals(""))
					_fontSize.setText("Enter Font Size");
			}

			@Override
			public void mousePressed(MouseEvent e) {
				//Clear on click
				_fontSize.setText("");
			}

			@Override
			public void mouseReleased(MouseEvent e) {}
			
		});
		_panel.add(_fontSize);
		
		//Forward Info
		_forwardLabel = new JLabel("Forward: " + KeyEvent.getKeyText(KeyEvent.VK_RIGHT));
		_forwardLabel.setFont(new Font("Sans Seriff",Font.PLAIN,20));
		_forwardLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		_panel.add(_forwardLabel);
		_forwardButton = new SetupButton("Change Forward Option");
		_forwardButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(_currentState == RecordingState.ExpectBackward)
					if(_backOption == null)
						_backLabel.setText("Back: " + KeyEvent.getKeyText(KeyEvent.VK_RIGHT));
					else
						_backLabel.setText("Back: " + KeyEvent.getKeyText(_backOption.getKeyCode()));
				_currentState = RecordingState.ExpectForward;
				_forwardLabel.setText("Forward: Click key to set");
				_panel.repaint();
				_panel.requestFocusInWindow();

			}			
		});
		
		//Backward Info
		_backLabel = new JLabel("Backward: " + KeyEvent.getKeyText(KeyEvent.VK_LEFT));
		_backLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		_backLabel.setFont(new Font("Sans Seriff",Font.PLAIN,20));
		_panel.add(_backLabel);
		_backButton = new SetupButton("Change Back Option");
		_backButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(_currentState == RecordingState.ExpectForward)
					if(_backOption == null)
						_backLabel.setText("Forward: " + KeyEvent.getKeyText(KeyEvent.VK_LEFT));
					else
						_backLabel.setText("Forward: " + KeyEvent.getKeyText(_forwardOption.getKeyCode()));
				_currentState = RecordingState.ExpectBackward;
				_backLabel.setText("Backward: Click key to set");
				_panel.repaint();
				_panel.requestFocusInWindow();
			}
			
		});

		
		//Button to start show
		_start = new SetupButton("Start!");
		_start.setForeground(Color.CYAN);
		_start.setBackground(Color.black);
		_start.setFont(new Font("Sans Seriff",Font.PLAIN,30));
		_start.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int numChars;
				try{
					numChars = Integer.parseInt(_numCharacters.getText());
				} catch(NumberFormatException e){
					//default #
					numChars = 50;
				}
				try{
					int fontSize =Integer.parseInt(_fontSize.getText());
					_st.getPlayPanel().resetValues(fontSize);
				} catch(NumberFormatException e){}
				
				try{
					List<Text> allText = parse(_filepath.getText(), numChars);
					_st.getPlayPanel().resetValues(allText);
					_st.switchModes();
				} catch(FileNotFoundException e){
					_filepath.setText("File " + _fileName + " not found!");

				} catch(IOException e){
					_filepath.setText("Something went wrong while parsing file " + _fileName + "!");
				} catch(ParseException e){
					_filepath.setText("Something went wrong while parsing file " + _fileName + "!");
				}
			}

		});
		
		_st.add(_panel);

		_panel.revalidate();
		_panel.repaint();
		_panel.requestFocusInWindow();
	}
	
	public List<Text> parse(String filename, int maxPerLine) throws FileNotFoundException, IOException, ParseException
	{
		List<Text> toReturn = new ArrayList<Text>();
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line = br.readLine();
		while(line != null){
			//Double line
			if(line.contains("^")){
				int index = line.indexOf("^");
				String[] split = {line.substring(0,index),line.substring(index + 1)};
				//More than one ^ not allowed
				if(split.length != 2){
					br.close();
					throw new ParseException();
				}
				
				if(split[0].length() > maxPerLine || split[1].length() > maxPerLine){
					int index0 = Math.min(split[0].length() - 1, maxPerLine - 1);
					int index1 = Math.min(split[1].length() - 1, maxPerLine - 1);
					while ( index0 > 0 && split[0].charAt(index0) != ' ')
						index0--;
					while (index1 > 0 && split[1].charAt(index1) != ' ')
						index1--;

					toReturn.add(new Text(split[0].substring(0,index0),split[1].substring(0,index1),true));
					line = split[0].substring(index0) + "^" + split[1].substring(index1);
				} else{
					
					//Otherwise simply create text
					toReturn.add(new Text(split[0],split[1],true));
					line = br.readLine();
				}
				
			} else{
				if(line.length() > maxPerLine)
				{
					//split into two lines, but split on word not character
					int index = maxPerLine - 1;
					while (line.charAt(index) != ' ' && index > 0)
						index--;
					String part1 = line.substring(0,index);
					if((line.length() - index) <= maxPerLine)
					{
						String part2 = line.substring(index + 1);
						toReturn.add(new Text(part1,part2,false));
						line = br.readLine();
					} else{
						int index2 = index + maxPerLine;
						while (line.charAt(index2) != ' ' && index2 > index)
							index2--;
						String part2 = line.substring(index + 1, index2);

						toReturn.add(new Text(part1, part2,false));
						line = line.substring(index2);
					}
				} else{
					//Otherwise, simply create text
					toReturn.add(new Text(line));
					line = br.readLine();
				}
			}
		}
		br.close();
		return toReturn;
	}
	
	public void swapIn(){
		_panel.setVisible(true);
		_panel.revalidate();
		_panel.repaint();
		_panel.grabFocus();
	}
	
	public void swapOut(){
		_panel.setVisible(false);
	}


}
