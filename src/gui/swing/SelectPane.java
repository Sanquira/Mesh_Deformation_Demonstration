package gui.swing;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import meshOperations.transformation.AbstractTransformation;
import net.miginfocom.swing.MigLayout;

public class SelectPane extends JScrollPane
{

	private CopyOnWriteArrayList<AbstractTransformation> list;
	private ButtonGroup btns = new ButtonGroup();
	private JPanel contentPane = new JPanel();
	private JPanel listPane;
	public int selected = 0;

	public SelectPane(CopyOnWriteArrayList<AbstractTransformation> copyOnWriteArrayList)
	{
		this.list = copyOnWriteArrayList;
		initialCreate();
	}

	private void initialCreate()
	{
		contentPane.setLayout(new MigLayout("nogrid,fillx", "[]", "[grow 200,fill][]"));

		listPane = new JPanel();
		listPane.setLayout(new MigLayout("wrap 1 ", "[]", "[]"));
		updateList();
		contentPane.add(listPane, "wrap");

		JPanel controlPane = new JPanel();
		controlPane.setLayout(new GridLayout(1, 3, 10, 10));
		JButton up = new JButton("Nahoru");
		up.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				ButtonModel model = btns.getSelection();
				if (model != null)
				{
					Integer pos = Integer.valueOf(model.getActionCommand());
					if (pos != 0)
					{
						selected = pos - 1;
						shift(pos);
					}
				}
			}

		});
		JButton down = new JButton("Dolů");
		down.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				ButtonModel model = btns.getSelection();
				if (model != null)
				{
					Integer pos = Integer.valueOf(model.getActionCommand());
					if (pos < list.size() - 1)
					{
						selected = pos + 1;
						shift(pos);
					}
				}
			}

		});
		JButton delete = new JButton("Smaž");
		delete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				ButtonModel model = btns.getSelection();
				if (model != null)
				{
					Integer pos = Integer.valueOf(model.getActionCommand());
					delete(pos);
				}
			}

		});
		controlPane.add(down);
		controlPane.add(up);
		contentPane.add(controlPane, "wrap");
		contentPane.add(delete);

		this.setViewportView(contentPane);
	}

	private JPanel constructPanel(AbstractTransformation t, int i)
	{
		JPanel result = new JPanel(new BorderLayout());

		JCheckBox check = new JCheckBox();
		// System.out.println(i == selected);
		btns.add(check);
		check.setSelected(i == selected);
		check.setActionCommand(String.valueOf(i));
		result.add(check, BorderLayout.WEST);

		JLabel label = new JLabel(t.getName());
		label.setHorizontalAlignment(JLabel.LEFT);
		result.add(label, BorderLayout.CENTER);

		result.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e)
			{
				new EditFrame(t, true).setVisible(true);
			}

		});
		return result;
	}

	public void shift(int pos)
	{
		AbstractTransformation a = list.remove(pos);
		if (selected == list.size())
		{
			list.add(a);
		} else
		{
			list.add(selected, a);
		}
		// System.out.println(list);
		updateList();
	}

	public void delete(int pos)
	{
		if (pos < 0 || pos >= list.size())
			return;
		list.remove(pos);
		updateList();
	}

	public void updateList()
	{
		listPane.removeAll();
		for (int i = 0; i < list.size(); i++)
		{
			// System.out.println(list.get(i).getName());
			listPane.add(constructPanel(list.get(i), i), "growx");
		}
		this.revalidate();
		this.repaint();
	}

	public CopyOnWriteArrayList<AbstractTransformation> getList()
	{
		return list;
	}

}
