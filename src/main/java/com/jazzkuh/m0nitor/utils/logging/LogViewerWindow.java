package com.jazzkuh.m0nitor.utils.logging;

import ch.qos.logback.classic.Level;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class LogViewerWindow extends JFrame {
    private static final Color BG      = new Color(20, 20, 20);
    private static final Color AURON   = new Color(0, 200, 200);
    private static final Color WEB     = new Color(200, 0, 200);
    private static final Color CORE    = new Color(204, 204, 204);
    private static final Color ERROR   = new Color(220, 50, 50);
    private static final Color WARN    = new Color(220, 180, 0);

    private final JTextPane textPane = new JTextPane();
    private final StyledDocument doc  = textPane.getStyledDocument();

    public LogViewerWindow() {
        super("M0NITOR Log Viewer");
        setSize(960, 540);
        setDefaultCloseOperation(HIDE_ON_CLOSE);

        textPane.setEditable(false);
        textPane.setBackground(BG);
        textPane.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        textPane.setForeground(CORE);

        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(BG);

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> {
            try { doc.remove(0, doc.getLength()); } catch (BadLocationException ignored) {}
        });

        JButton hideButton = new JButton("Hide");
        hideButton.addActionListener(e -> setVisible(false));

        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        bottomBar.setBackground(new Color(35, 35, 35));
        bottomBar.add(clearButton);
        bottomBar.add(hideButton);

        add(scrollPane, BorderLayout.CENTER);
        add(bottomBar, BorderLayout.SOUTH);

        // Stream new entries to the document only while the window is visible
        LogAppender.setListener(entry -> {
            if (isVisible()) SwingUtilities.invokeLater(() -> appendEntry(entry));
        });
    }

    private void loadBuffer() {
        try { doc.remove(0, doc.getLength()); } catch (BadLocationException ignored) {}
        for (LogAppender.LogEntry entry : LogAppender.getEntries()) appendEntry(entry);
    }

    private void appendEntry(LogAppender.LogEntry entry) {
        Color color = switch (entry.label()) {
            case "AURON" -> AURON;
            case "WEB"   -> WEB;
            default      -> CORE;
        };
        if (entry.level() == Level.ERROR_INT)      color = ERROR;
        else if (entry.level() == Level.WARN_INT)  color = WARN;

        SimpleAttributeSet attrs = new SimpleAttributeSet();
        StyleConstants.setForeground(attrs, color);
        StyleConstants.setBackground(attrs, BG);

        try {
            doc.insertString(doc.getLength(), entry.text() + "\n", attrs);
        } catch (BadLocationException ignored) {}

        textPane.setCaretPosition(doc.getLength());
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) loadBuffer();
        super.setVisible(visible);
        if (visible) {
            toFront();
            requestFocus();
        }
    }
}