package supertitles;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.*;

public class Supertitles extends JFrame{
	
	private Mode _currMode;
	private SetupPanel _setupPanel;
	private PlayPanel _playPanel;
	private List<Text> _allText;
	private int _currIndex;
	public enum Mode{
		Setup,Play;
	}
	
	public Supertitles(){
		super("Supertitles");
		_currIndex = 0;
		setupGUI();
	}

	public void setupGUI(){
		_currMode = Mode.Setup;

		//Properties of the frame
		setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1000,650);
		_setupPanel = new SetupPanel(this);
		_playPanel = new PlayPanel(this,KeyEvent.VK_RIGHT,KeyEvent.VK_LEFT);
		
		repaint();
	}
	
	public void switchModes(){
		switch(_currMode){
		case Setup:
			//Switch to play mode
			_setupPanel.swapOut();
			_playPanel.swapIn();
			_currMode = Mode.Play;
			break;
		case Play:
			//Switch to setup mode
			_playPanel.swapOut();
			_setupPanel.swapIn();
			_currMode = Mode.Setup;
		}
	}
	
	public PlayPanel getPlayPanel(){
		return _playPanel;
	}
	public static void main(String[] args){
		new Supertitles();
	}
}
