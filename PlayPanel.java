package supertitles;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

import supertitles.Supertitles.Mode;

public class PlayPanel{
	private Supertitles _st;
	private JPanel _panel;
	private JLabel _previous, _current, _currBlackout, _next,_helpMessage;
	private List<Text> _allText;
	private int _forwardKey, _backKey,_currIndex;
	private JButton _marker, _blackout, _jumpFront, _jumpBack,_jumpPrev,_jumpNext,_download,_returnToSetup;
	private boolean _blackedOut;
	private String _defaultMessage;
	
	private class HoverButton extends JButton{
		public HoverButton(String text, final String hoverMessage){
			super(text);
			this.addMouseListener(new MouseListener(){

				@Override
				public void mouseClicked(MouseEvent arg0) {}

				@Override
				public void mouseEntered(MouseEvent arg0) {
					_helpMessage.setText(hoverMessage);
				}

				@Override
				public void mouseExited(MouseEvent arg0) {
					_helpMessage.setText(_defaultMessage);
				}

				@Override
				public void mousePressed(MouseEvent arg0) {}

				@Override
				public void mouseReleased(MouseEvent arg0) {}
				
			});
		}
	}
	
	public PlayPanel(Supertitles s,int forward, int back){
		_forwardKey = forward;
		_backKey = back;
		_panel = new JPanel();
		_st = s;
		_blackedOut = false;
		_panel.setVisible(false);
		setupGUI();
	}

	public void resetValues(List<Text> allText){
		_allText = allText;
		_currIndex = 0;
		displayText();
	}

	public void resetValues(int option, boolean isForward){
		if(isForward)
			_forwardKey = option;
		else
			_backKey =option;
		
		_defaultMessage = "Forward: " + KeyEvent.getKeyText(_forwardKey) + " Backward: " + KeyEvent.getKeyText(_backKey);
		_helpMessage.setText(_defaultMessage);
		_panel.repaint();
	}
	
	
	//Reset font size
	public void resetValues(int size){
		_current.setFont(new Font(_current.getFont().getFontName(),_current.getFont().getStyle(),size));
	}
	public void setupGUI(){
		_panel.setLayout(new BoxLayout(_panel,BoxLayout.Y_AXIS));
		_panel.setBackground(new Color(0,0,0));
		_panel.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent e) {
				if(_allText != null){
					if(e.getKeyCode() == _forwardKey){
						//move forward
						_currIndex++;
						if(_currIndex >= _allText.size())
							_currIndex = _allText.size() - 1;
					} else if(e.getKeyCode() == _backKey){
						//move backwards
						_currIndex--;
						if(_currIndex < 0)
							_currIndex = 0;
					}

									
					
					displayText();
					_panel.repaint();
					_panel.revalidate();
				}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {}

			@Override
			public void keyTyped(KeyEvent arg0) {}

		});
		
		
		//Previous Label
		_previous = new JLabel();
		_previous.setForeground(Color.WHITE);
		_previous.setFont(new Font(_previous.getFont().getName(),_previous.getFont().getStyle(),20));
		_previous.setHorizontalAlignment(JLabel.CENTER);
		_panel.add(_previous);

		//Current Label
		_current = new JLabel();
		_current.setForeground(Color.WHITE);
		_current.setFont(new Font(_current.getFont().getName(),_current.getFont().getStyle(),35));
		_current.setHorizontalAlignment(JLabel.CENTER);
		_panel.add(_current);
		
		//Curr Blackout Label
		_currBlackout = new JLabel();
		_currBlackout.setForeground(Color.RED);
		_currBlackout.setFont(new Font(_currBlackout.getFont().getName(),_currBlackout.getFont().getStyle(),20));
		_currBlackout.setHorizontalAlignment(JLabel.CENTER);
		_currBlackout.setVisible(false);
		_panel.add(_currBlackout);

		//Next Label
		_next = new JLabel();
		_next.setForeground(Color.WHITE);
		_next.setFont(new Font(_next.getFont().getName(),_next.getFont().getStyle(),20));
		_next.setHorizontalAlignment(JLabel.CENTER);
		_panel.add(_next);

		//Marker
		_marker = new HoverButton("Mark for edits","You can download a file with all the marks bellow");
		_marker.setHorizontalAlignment(JButton.CENTER);
		_marker.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				_allText.get(_currIndex).changeMarkStatus();
				if(_allText.get(_currIndex).isMarked())
					_marker.setText("Unmark");
				else
					_marker.setText("Mark for edits");
				
				_panel.repaint();
				_panel.requestFocusInWindow();

			}
			
		});
		_panel.add(_marker);
		
		//Blackout
		_blackout = new HoverButton("Blackout","Allows you to find your place by hiding the text from the projection");
		_blackout.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(!_blackedOut){
					_current.setForeground(Color.BLACK);
					_blackout.setText("Resume");
					_currBlackout.setVisible(true);
					_blackedOut = true;
				} else{
					_current.setForeground(Color.WHITE);
					_blackout.setText("Blackout");
					_currBlackout.setVisible(false);
					_blackedOut = false;
				}
				_panel.requestFocusInWindow();
			}
			
		});
		_panel.add(_blackout);
		
		//Jump to beginning button
		_jumpFront = new HoverButton("Go To Beginning","Jump to the beginning of the show");
		_jumpFront.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				_currIndex = 0;
				displayText();
				_panel.requestFocusInWindow();
			}
			
		});
		_panel.add(_jumpFront);
		
		
		//Jump to end button
		_jumpBack = new HoverButton("Go To End","Jump to the end of the show");
		_jumpBack.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				_currIndex = _allText.size() - 1;
				displayText();
				_panel.requestFocusInWindow();
			}
		});
		_panel.add(_jumpBack);
		
		//Jump to previous song
		_jumpPrev = new HoverButton("Previous Song","Skip to the previous blank line");
		_jumpPrev.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				for(int i=_currIndex - 1; i >= 0; i--){
					if(_allText.get(i).isBlank()){
						_currIndex = i;
						displayText();
						break;
					}
				}
				_panel.requestFocusInWindow();
			}
			
		});
		_panel.add(_jumpPrev);
		
		_jumpNext = new HoverButton("Next Song","Skip to the next blank line");
		_jumpNext.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				for(int i=_currIndex + 1; i< _allText.size(); i++){
					if(_allText.get(i).isBlank()){
						_currIndex = i;
						displayText();
						break;
					}
				}
				_panel.requestFocusInWindow();
			}
			
		});
		_panel.add(_jumpNext);
	
		_download = new HoverButton("Download Marked File","Click to download a file with *s next to all marked lines");
		_download.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				downloadFile();
				_panel.requestFocusInWindow();
			}
			
		});
		_panel.add(_download);
		
		//Return to setup
		_returnToSetup = new HoverButton("Return to Setup Screen","Click to return to setup Screen");
		_returnToSetup.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				_st.switchModes();
				_panel.requestFocusInWindow();
			}
			
		});
		_panel.add(_returnToSetup);
		
		_defaultMessage = "Forward: " + KeyEvent.getKeyText(_forwardKey) + " Backward: " + KeyEvent.getKeyText(_backKey);
		_helpMessage = new JLabel(_defaultMessage);
		_helpMessage.setForeground(Color.white);
		_panel.add(_helpMessage);
		
		_st.add(_panel);
		_panel.revalidate();
		_panel.repaint();
		
	}

	public void displayText(){
		if(_currIndex == 0)
			_previous.setText("<html> <br><center> Beginning of Show </center> <br> </html>");
		else
			setText(_previous,_allText.get(_currIndex - 1));

		setText(_current,_allText.get(_currIndex));
		setText(_currBlackout,_allText.get(_currIndex));
		if(_currIndex == (_allText.size() - 1 ))
			_next.setText("<html> <center> <br>End of Show <br> </center> </html>");
		else
			setText(_next,_allText.get(_currIndex + 1));
		
		updateMarks();
	}

	public void setText(JLabel label, Text t){
		//label.setText(t.getFirst());
		label.setText("<html> <center> <br>" + t.getFirst() + "<br>" + t.getSecond() + "<br></center></html>");

	}

	public void swapIn(){
		_panel.setVisible(true);
		_panel.grabFocus();
		_panel.revalidate();
		_panel.repaint();
		_panel.grabFocus();
	}

	public void swapOut(){
		_panel.setVisible(false);
	}
	
	public void downloadFile(){
		BufferedWriter bw;
		try{
			JFileChooser fc = new JFileChooser();
		    int option = fc.showSaveDialog(_panel);  
			if(option == JFileChooser.APPROVE_OPTION){  
				if(fc.getSelectedFile()!=null){  
					File fileToSave = fc.getSelectedFile();  
				
					bw = new BufferedWriter(new FileWriter(fileToSave));
					for(int i=0; i< _allText.size(); i++){
						bw.write(_allText.get(i).printToFile());
						bw.newLine();
					}
					bw.flush();
					bw.close();
				}
			}
		} catch(IOException e){}
	}
	public void updateMarks(){
		//Change marked button
		if(_allText.get(_currIndex).isMarked())
			_marker.setText("Unmark");
		else
			_marker.setText("Mark for edits");
	}

}
