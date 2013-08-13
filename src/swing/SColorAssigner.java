package swing;

import graphic.ColoredComponent;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import utility.PersonalizedIcon;
import utility.Utility;
import change.BufferColor;
import change.Change;

/**
 * Show a dialog for choosing a color.
 * 
 * @author David Miserez
 * @version 1.0 - 25.07.2011
 */
public class SColorAssigner extends JDialog {
  private static final long serialVersionUID = -1975479020681307211L;
  private JColorChooser colorChooser;
  private final JPanel contentPanel = new JPanel();
  private ColoredComponent[] components;

  private interface ObtainColor {
    public Color getColor(ColoredComponent c);
  }

  /**
   * Create the dialog.
   */
  public SColorAssigner(ColoredComponent... components) {
    // Generated by WindowBuilder from Google.
    Utility.setRootPaneActionOnEsc(getRootPane(), new AbstractAction() {
      private static final long serialVersionUID = -9137055482704631902L;

      public void actionPerformed(ActionEvent e) {
        setVisible(false);
      }
    });

    this.components = components;
    setModalityType(ModalityType.APPLICATION_MODAL);
    setResizable(false);
    setTitle("Slyum - Choose a color...");
    setIconImage(PersonalizedIcon.getLogo().getImage());
    setBounds(100, 100, 635, 421);
    setLocationRelativeTo(Slyum.getInstance());
    getContentPane().setLayout(new BorderLayout());
    contentPanel.setLayout(new FlowLayout());
    contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
    getContentPane().add(contentPanel, BorderLayout.CENTER);
    {
      colorChooser = new JColorChooser(components[0].getColor());
      contentPanel.add(colorChooser);
    }
    {
      final JPanel buttonPane = new JPanel();
      buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
      buttonPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
      getContentPane().add(buttonPane, BorderLayout.SOUTH);

      {
        final JButton defaultButton = new JButton("Default");
        defaultButton.setActionCommand("default color");
        defaultButton.addActionListener(new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent e) {
            changeComponentsColor(new ObtainColor() {
              @Override
              public Color getColor(ColoredComponent c) {
                return c.getDefaultColor();
              }
            });
            setVisible(false);
          }
        });
        buttonPane.add(defaultButton);
        buttonPane.add(Box.createHorizontalGlue());
      }
      {
        final JButton okButton = new JButton("Ok");
        okButton.addActionListener(new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent e) {
            changeComponentsColor(new ObtainColor() {
              @Override
              public Color getColor(ColoredComponent c) {
                return colorChooser.getColor();
              }
            });
            setVisible(false);
          }
        });
        okButton.setActionCommand("OK");
        buttonPane.add(okButton);

        getRootPane().setDefaultButton(okButton);
        buttonPane.add(Box.createHorizontalStrut(10));
      }
      {
        final JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent e) {
            setVisible(false);
          }
        });
        cancelButton.setActionCommand("Cancel");
        buttonPane.add(cancelButton);
      }
    }

    setVisible(true);
  }

  private void changeComponentsColor(ObtainColor o) {
    boolean isRecord = Change.isRecord();
    Change.record();
    for (ColoredComponent c : components) {
      // Set default style before save color.
      c.setDefaultStyle();
      Change.push(new BufferColor(c));
      c.setColor(o.getColor(c));
      Change.push(new BufferColor(c));
    }
    if (!isRecord) Change.stopRecord();
  }
}
