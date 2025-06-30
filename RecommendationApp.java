package com.codtech;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RecommendationApp extends JFrame {
    private JTextField userIdField;
    private JTextField countField;
    private JTextArea outputArea;

    private RecommenderEngine engine;

    public RecommendationApp() {
        setTitle("AI-Based Recommendation System");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        try {
            engine = new RecommenderEngine("data.csv");  // Load from file system
        } catch (Exception e) {
            showError("Failed to load data: " + e.getMessage());
            return;
        }

        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        inputPanel.add(new JLabel("User ID:"));
        userIdField = new JTextField();
        inputPanel.add(userIdField);

        inputPanel.add(new JLabel("Number of Recommendations:"));
        countField = new JTextField();
        inputPanel.add(countField);

        JButton recommendButton = new JButton("Get Recommendations");
        inputPanel.add(recommendButton);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        recommendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getRecommendations();
            }
        });
    }

    private void getRecommendations() {
        String userIdText = userIdField.getText().trim();
        String countText = countField.getText().trim();

        if (userIdText.isEmpty() || countText.isEmpty()) {
            showError("Please enter both User ID and count.");
            return;
        }

        try {
            long userId = Long.parseLong(userIdText);
            int count = Integer.parseInt(countText);
            String recommendations = engine.getRecommendations((int)userId, count);
            outputArea.setText(recommendations);
        } catch (NumberFormatException ex) {
            showError("User ID and count must be numbers.");
        } catch (Exception ex) {
            showError("Error: " + ex.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new RecommendationApp().setVisible(true);
        });
    }
}