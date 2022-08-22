package src.program_entry;

import java.awt.*;
import javax.swing.*;
import src.component.BasicUI;

public class TestMain
{
	public static void main(String args[]) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		Font defaultFont = new Font("System", Font.PLAIN, 14);
		
		String UI[]= {"Button.font","CheckBox.font","RadioButton.font","ToolTip.font","ComboBox.font",
				  "Label.font","List.font","Table.font","TableHeader.font","MenuBar.font","Menu.font",
				  "MenuItem.font","PopupMenu.font","Tree.font","ToolBar.font"};
		for(int i=0;i<UI.length;i++)
			UIManager.put(UI[i], defaultFont);
		//UIManager.setLookAndFeel( UIManager.WindowsLookAndFeel()); 
	    BasicUI basicUI = new BasicUI();
	    basicUI.setTitle("Mini PS");
	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        basicUI.setLocationRelativeTo(null);
        basicUI.setVisible(true);

	}
}
