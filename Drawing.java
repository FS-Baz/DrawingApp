import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Drawing extends JFrame {
    int width = 600;
    int height = 600;
    JPanel p;
    JButton text = new JButton("Add text"),
            vector = new JButton("Vector"),
            penBtn = new JButton("Pen"),
            eraserBtn = new JButton("Eraser"),
            transformButton = new JButton("Transform"),
            drawVectorButton = new JButton("Vector By Equation"),
            eraserButton = new JButton("Eraser"),
            colorPickerButton = new JButton("Choose Color");
    JPanel westPanel = new JPanel(),
            northPanel = new JPanel(),
            centerPanel = new JPanel();
    JTextField rectLength = new JTextField(),
            rectWidth = new JTextField(),
            transoption = new JTextField(),
            newX = new JTextField(),
            newY = new JTextField();
    private Color currentColor = Color.blue;
    private int size = 1;
    private int lastX, lastY;
    ArrayList<Point> points = new ArrayList<Point>();
    ArrayList<Rectangle> rects = new ArrayList<Rectangle>();
    String[] shapes = {"Shapes", "Rectangle", "Circle", "Polygon"};
    String[] transformOptions = {"Transformation", "Move", "Scale", "Rotate"};
    Boolean rect = false,
            cerc = false,
            pol = false,
            pen = true,
            txt = false,
            fill = false,
            move = false,
            scale = false,
            rotate = false,
            eraser = false;
    GridBagConstraints gbc = new GridBagConstraints();
    Font f;
    String str;
    int numOfPoints = 0;

    Drawing() {
        westPanel.setBackground(Color.LIGHT_GRAY);
        westPanel.setLayout(new GridBagLayout());
        JComboBox<String> transform = new JComboBox<>(transformOptions);
        gbc.gridy = 0;
        westPanel.add(transform, gbc);
        gbc.gridy = 2;
        westPanel.add(vector, gbc);
        gbc.gridy = 3;
        westPanel.add(drawVectorButton, gbc);
        gbc.gridy = 1;
        westPanel.add(transformButton, gbc);
        transform.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (transform.getSelectedIndex()) {
                    case 0:
                        break;
                    case 1:
                        Object[] moveOptions = {
                                "Select shape (max is:" + rects.size() + ")", transoption,
                                "New x", newX,
                                "New y", newY
                        };
                        JOptionPane.showConfirmDialog(null, moveOptions, "Enter the new position", JOptionPane.OK_CANCEL_OPTION);
                        scale = false;
                        rotate = false;
                        move = true;
                        break;
                    case 2:
                        Object[] scaleOptions = {
                                "Select shape (max is:" + rects.size() + ")", transoption,
                                "X scale factor", newX,
                                "Y scale factor", newY
                        };
                        JOptionPane.showConfirmDialog(null, scaleOptions, "Enter scale factors", JOptionPane.OK_CANCEL_OPTION);
                        move = false;
                        rotate = false;
                        scale = true;
                        break;
                    case 3:
                        Object[] rotateOptions = {
                                "Select shape (max is:" + rects.size() + ")", transoption,
                                "Angle in degrees", newX
                        };
                        JOptionPane.showConfirmDialog(null, rotateOptions, "Enter rotation degree", JOptionPane.OK_CANCEL_OPTION);
                        move = false;
                        scale = false;
                        rotate = true;
                        break;
                    default:
                        break;
                }
            }
        });
        transformButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Graphics g = centerPanel.getGraphics();
                Graphics2D G2d = (Graphics2D) g;
                if (move) {
                    int z = Integer.parseInt(transoption.getText()) - 1;
                    int x = Integer.parseInt(newX.getText());
                    int y = Integer.parseInt(newY.getText());

                    G2d.setColor(centerPanel.getBackground());
                    G2d.fillRect(rects.get(z).x, rects.get(z).y, (int) rects.get(z).getWidth() + rects.get(z).x, (int) rects.get(z).getHeight() + rects.get(z).y);
                    rects.get(z).translate(x, y);
                    G2d.setColor(currentColor);
                    if (fill) {
                        if (rect)
                            G2d.fill(rects.get(z));
                        else if (cerc)
                            G2d.fillOval(rects.get(z).x, rects.get(z).y, rects.get(z).width, rects.get(z).height);
                    } else {
                        if (rect)
                            G2d.draw(rects.get(z));
                        else if (cerc)
                            G2d.drawOval(rects.get(z).x, rects.get(z).y, rects.get(z).width, rects.get(z).height);

                    }
                    rects.remove(z);
                } else if (scale) {
                    int z = Integer.parseInt(transoption.getText()) - 1;
                    int x = Integer.parseInt(newX.getText());
                    int y = Integer.parseInt(newY.getText());

                    G2d.setColor(centerPanel.getBackground());
                    G2d.fillRect(rects.get(z).x, rects.get(z).y, (int) rects.get(z).getHeight() + rects.get(z).y, (int) rects.get(z).getWidth() + rects.get(z).x);
                    G2d.setColor(currentColor);
                    if (fill) {
                        G2d.scale(x, y);
                        G2d.fill(rects.get(z));
                    } else {
                        G2d.scale(x, y);
                        G2d.draw(rects.get(z));
                    }
                    rects.remove(z);

                    G2d.scale(1, 1);
                } else if (rotate) {
                    int z = Integer.parseInt(transoption.getText()) - 1;
                    double x = Double.parseDouble(newX.getText());
                    G2d.setColor(centerPanel.getBackground());
                    G2d.fillRect(rects.get(z).x, rects.get(z).y, (int) rects.get(z).getHeight() + rects.get(z).y, (int) rects.get(z).getWidth() + rects.get(z).x);
                    G2d.setColor(currentColor);
                    if (fill) {
                        G2d.rotate(Math.toRadians(x));
                        G2d.fill(rects.get(z));
                    } else {
                        G2d.rotate(Math.toRadians(x));
                        G2d.draw(rects.get(z));

                    }
                    rects.remove(z);
                    G2d.rotate(0);
                }
            }
        });
        drawVectorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField x1 = new JTextField();
                JTextField y1 = new JTextField();
                JTextField x2 = new JTextField();
                JTextField y2 = new JTextField();

                Object[] fields = {
                        "X1", x1,
                        "Y1", y1,
                        "X2", x2,
                        "Y2", y2
                };
                JOptionPane.showConfirmDialog(null, fields, "Enter vector coordinates", JOptionPane.OK_CANCEL_OPTION);
                Graphics g = centerPanel.getGraphics();
                Graphics2D G2d = (Graphics2D) g;
                G2d.setColor(currentColor);
                int X1 = Integer.parseInt(x1.getText());
                int Y1 = Integer.parseInt(y1.getText());
                int X2 = Integer.parseInt(x2.getText());
                int Y2 = Integer.parseInt(y2.getText());
                G2d.drawLine(X1, Y1, X2, Y2);
            }
        });
        penBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                penBtn.setFocusable(false);
                pen = true;
                cerc = false;
                pol = false;
                txt = false;
                rect = false;
                eraser = false;
                if (pen) {
                    penBtn.setBackground(Color.GREEN);
                    eraserBtn.setBackground(new JButton().getBackground());
                    text.setBackground(new JButton().getBackground());
                }
            }
        });
        eraserBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eraserBtn.setFocusable(false);
                pen = false;
                cerc = false;
                pol = false;
                txt = false;
                rect = false;
                eraser = true;
                if (eraser) {
                    eraserBtn.setBackground(Color.GREEN);
                    penBtn.setBackground(new JButton().getBackground());
                    text.setBackground(new JButton().getBackground());
                }
            }
        });
        JPanel southPanel = new JPanel();
        southPanel.setBackground(Color.LIGHT_GRAY);
        JPanel eastPanel = new JPanel();
        eastPanel.setBackground(Color.LIGHT_GRAY);
        JComboBox<String> menu = new JComboBox<>(shapes);
        eastPanel.setLayout(new GridBagLayout());
        gbc.gridy = 0;
        eastPanel.add(menu, gbc);
        gbc.gridy = 1;
        eastPanel.add(text, gbc);
        JCheckBox fillBox = new JCheckBox("Fill");
        fillBox.setBackground(eastPanel.getBackground());
        gbc.gridy = 3;
        eastPanel.add(fillBox, gbc);
        fillBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fillBox.setFocusable(false);
                if (fillBox.isSelected()) {
                    fill = true;
                } else {
                    fill = false;
                }
            }
        });
        text.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                text.setFocusable(false);
                pen = false;
                rect = false;
                cerc = false;
                pol = false;
                txt = true;
                if (txt) {
                    text.setBackground(Color.green);
                    eraserBtn.setBackground(new JButton().getBackground());
                    penBtn.setBackground(new JButton().getBackground());
                }
                f = chooseFont();
                new JOptionPane();
                str = JOptionPane.showInputDialog("Enter a string:");
                System.out.println(str);
            }
        });
        vector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField x1 = new JTextField();
                JTextField y1 = new JTextField();

                Object[] fields = {
                        "X1", x1,
                        "Y1", y1,
                };
                JOptionPane.showConfirmDialog(null, fields, "Enter vector coordinates", JOptionPane.OK_CANCEL_OPTION);
                Graphics g = centerPanel.getGraphics();
                Graphics2D G2d = (Graphics2D) g;
                G2d.setFont(f);
                G2d.setColor(currentColor);
                int X1 = (Integer.parseInt(x1.getText()) + centerPanel.getWidth()) / 2;
                int Y1 = (Integer.parseInt(y1.getText()) + centerPanel.getHeight()) / 2;
                System.out.println(X1);
                System.out.println(Y1);
                if (Integer.parseInt(y1.getText()) > 0) {
                    Y1 -= Integer.parseInt(y1.getText());
                    System.out.println("Hi");
                    System.out.println(Y1);
                }
                if (Integer.parseInt(y1.getText()) < 0) {
                    Y1 += Integer.parseInt(y1.getText()) * -1;
                    System.out.println("Hi");
                    System.out.println(Y1);
                }
                G2d.drawLine(centerPanel.getWidth() / 2, centerPanel.getHeight() / 2, X1, Y1);
            }
        });
        menu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (menu.getSelectedIndex()) {
                    case 0:
                        pen = true;
                        cerc = false;
                        pol = false;
                        txt = false;
                        rect = false;
                        eraser = false;
                        break;
                    case 1:
                        pen = false;
                        cerc = false;
                        pol = false;
                        txt = false;
                        rect = true;
                        eraser = false;
                        Object[] rectInfo = {
                                "Length", rectLength,
                                "Width", rectWidth
                        };
                        JOptionPane.showConfirmDialog(null, rectInfo, "Enter vector coordinates", JOptionPane.OK_CANCEL_OPTION);
                        break;
                    case 2:
                        pen = false;
                        rect = false;
                        pol = false;
                        txt = false;
                        eraser = false;
                        cerc = true;

                        Object[] cercInfo = {
                                "Length", rectLength,
                                "Width", rectWidth
                        };
                        JOptionPane.showConfirmDialog(null, cercInfo, "Enter vector coordinates", JOptionPane.OK_CANCEL_OPTION);
                        break;
                    case 3:
                        pen = false;
                        rect = false;
                        cerc = false;
                        txt = false;
                        pol = true;
                        eraser = false;
                        new JOptionPane();
                        numOfPoints = Integer.parseInt(JOptionPane.showInputDialog("Enter the number of points:"));
                        System.out.println(numOfPoints);
                        break;
                    default:
                        break;
                }
            }
        });
        centerPanel.setBackground(Color.WHITE);
        centerPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                lastX = e.getX();
                lastY = e.getY();
                Graphics g = centerPanel.getGraphics();
                Graphics2D G2d = (Graphics2D) g;
                G2d.setFont(f);
                G2d.setColor(currentColor);
                if (rect) {
                    if (fill) {
                        rects.add(new Rectangle(e.getX(), e.getY(), Integer.parseInt(rectWidth.getText()), Integer.parseInt(rectLength.getText())));
                        G2d.fillRect(e.getX(), e.getY(), Integer.parseInt(rectWidth.getText()), Integer.parseInt(rectLength.getText()));
                    } else
                        G2d.drawRect(e.getX(), e.getY(), Integer.parseInt(rectWidth.getText()), Integer.parseInt(rectLength.getText()));
                    rects.add(new Rectangle(e.getX(), e.getY(), Integer.parseInt(rectWidth.getText()), Integer.parseInt(rectLength.getText())));
                } else if (txt) {
                    G2d.setFont(f);
                    G2d.drawString(str, e.getX(), e.getY());
                } else if (cerc) {
                    if (fill) {
                        rects.add(new Rectangle(e.getX(), e.getY(), Integer.parseInt(rectWidth.getText()), Integer.parseInt(rectLength.getText())));
                        G2d.fillOval(e.getX(), e.getY(), Integer.parseInt(rectWidth.getText()), Integer.parseInt(rectLength.getText()));
                    } else
                        G2d.drawOval(e.getX(), e.getY(), Integer.parseInt(rectWidth.getText()), Integer.parseInt(rectLength.getText()));
                    rects.add(new Rectangle(e.getX(), e.getY(), Integer.parseInt(rectWidth.getText()), Integer.parseInt(rectLength.getText())));

                } else if (pol) {
                    points.add(new Point(e.getX(), e.getY()));
                    G2d.fillOval(e.getX(), e.getY(), 5, 5);
                    if (points.size() == numOfPoints)
                        for (int i = 0; i < points.size() - 1; i++) {

                            G2d.drawLine((int) points.get(i).getX(), (int) points.get(i).getY(), (int) points.get(i + 1).getX(), (int) points.get(i + 1).getY());
                        }
                    G2d.drawLine((int) points.get(numOfPoints - 1).getX(), (int) points.get(numOfPoints - 1).getY(), (int) points.get(0).getX(), (int) points.get(0).getY());
                    numOfPoints = 0;
                    points.removeAll(points);
                    menu.setSelectedIndex(0);
                }
                if (pen) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        G2d.setColor(currentColor);
                        G2d.fillOval(e.getX(), e.getY(), size, size);
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        erase(e.getX(), e.getY());
                    }
                }
                lastX = e.getX();
                lastY = e.getY();
            }
        });
        centerPanel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Graphics g = centerPanel.getGraphics();
                Graphics2D G2d = (Graphics2D) g;
                if (pen) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        ((Graphics2D) G2d).setStroke(new BasicStroke(size));
                        G2d.setColor(currentColor);
                        f = G2d.getFont();
                        G2d.drawLine(lastX, lastY, e.getX(), e.getY());
                        lastX = e.getX();
                        lastY = e.getY();
                    }
                } else if (eraser) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        erase(e.getX(), e.getY());
                    }
                }
                lastX = e.getX();
                lastY = e.getY();
            }
        });
        northPanel.setBackground(Color.LIGHT_GRAY);
        penBtn.setBackground(Color.green);
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, 100, 1);
        JSpinner Size = new JSpinner(spinnerModel);
        Size.addChangeListener(e -> size = (int) Size.getValue());
        northPanel.add(new JLabel("Size:"));
        northPanel.add(Size);
        colorPickerButton.addActionListener(e -> chooseColor());
        colorPickerButton.setFocusable(false);
        northPanel.add(colorPickerButton);
        northPanel.add(penBtn);
        northPanel.add(eraserBtn);
        this.setSize(width, height);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.add(westPanel, BorderLayout.WEST);
        this.add(northPanel, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(eastPanel, BorderLayout.EAST);
        this.add(southPanel, BorderLayout.SOUTH);
        this.setLocationRelativeTo(null);
        this.setTitle("Drawing app");
        this.setVisible(true);
    }

    private void chooseColor() {
        Color newColor = JColorChooser.showDialog(this, "Choose a color", currentColor);
        if (newColor != null) {
            currentColor = newColor;
        }
    }

    private Font chooseFont() {
        f = FontChooser.showDialog(centerPanel, "Choose a font");
        return f;
    }

    private void erase(int x, int y) {
        Graphics G2d = centerPanel.getGraphics();
        G2d.setColor(centerPanel.getBackground());
        G2d.fillOval(x - size / 2, y - size / 2, size, size);
    }

    public static void main(String[] args) {
        new Drawing();
    }
}