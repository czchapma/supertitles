package supertitles;

public class Text {
	private boolean _marked;
	private boolean _grouped; //true if lines MUST be together
	private String _line1;
	private String _line2;
	
	//2 lines of text
	public Text(String part1, String part2, boolean grouped){
		_line1 = part1;
		_line2 = part2;
		_grouped = grouped;
		
		if(_line1.equals(_line2)){
			_line2 = "";
		}
		if(_line1.contains("*") || _line2.contains("*")){
			_marked = true;
			_line1 = removeStar(_line1);
			_line2 = removeStar(_line2);
		}
		else
			_marked = false;
	}
	
	//only 1 line of text
	public Text(String str)
	{
		_line1 = str;
		_line2 = "";
		if(_line1.contains("*")){
			System.out.println("Contains *");
			_marked = true;
			_line1 = removeStar(_line1);
		}
		else
			_marked = false;
	}
	
	public String getFirst(){
		return _line1;
	}
	
	public String getSecond(){
		return _line2;
	}
	
	public void changeMarkStatus(){
		_marked = !_marked;
	}
	
	public boolean isMarked(){
		return _marked;
	}
	
	public boolean isBlank(){
		return _line1.equals("") && _line2.equals("");
	}
	
	
	//Remove star from line
	public String removeStar(String s){
		int index = s.indexOf("*");
		if(index != -1){
			String temp = s.substring(0,index);
			if(index< (s.length() - 1))
				temp += s.substring(index + 1); 
			s = temp;
		}
		
		return s; 

	}
	
	//String to add as line in downloaded file
	public String printToFile(){
		String append = "";
		if(_marked)
			append = "*";
		if(_grouped)
			return _line1 + "^" + _line2 + append;
		else 
			return _line1 + _line2 + append;
	}
}
