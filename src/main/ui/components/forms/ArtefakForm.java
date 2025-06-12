package main.ui.components.forms;

import main.controller.ManajemenArtefakController;
import main.model.dto.ArtefakDto;
import main.model.enums.StatusArtefak;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Form untuk input/edit data artefak
public class ArtefakForm extends JDialog {
    private final ManajemenArtefakController controller;
    private ArtefakDto currentArtefak;
    
    // Form components
    private JTextField namaField;
    private JTextArea deskripsiArea;
    private JTextField asalDaerahField;
    private JTextField periodeField;
    private JComboBox<StatusArtefak> statusCombo;
    private JTextField gambarField;
    private JButton saveButton;
    private JButton cancelButton;
    
    public ArtefakForm(Frame parent, ManajemenArtefakController controller) {
        super(parent, "Form Artefak", true);
        this.controller = controller;
        initComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    // Initialize form components
    private void initComponents() {
        namaField = new JTextField(20);
        deskripsiArea = new JTextArea(4, 20);
        asalDaerahField = new JTextField(20);
        periodeField = new JTextField(20);
        statusCombo = new JComboBox<>(StatusArtefak.values());
        gambarField = new JTextField(20);
        saveButton = new JButton("Simpan");
        cancelButton = new JButton("Batal");
        
        deskripsiArea.setLineWrap(true);
        deskripsiArea.setWrapStyleWord(true);
    }
    
    // Setup form layout
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Nama Artefak
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(new JLabel("Nama Artefak:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(namaField, gbc);
        
        // Deskripsi
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(new JLabel("Deskripsi:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.BOTH;
        formPanel.add(new JScrollPane(deskripsiArea), gbc);
        
        // Asal Daerah
        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(new JLabel("Asal Daerah:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(asalDaerahField, gbc);
        
        // Periode
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Periode:"), gbc);
        gbc.gridx = 1;
        formPanel.add(periodeField, gbc);
        
        // Status
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        formPanel.add(statusCombo, gbc);
        
        // Gambar
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Gambar:"), gbc);
        gbc.gridx = 1;
        formPanel.add(gambarField, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        pack();
        setLocationRelativeTo(getParent());
    }
    
    // Setup event handlers
    private void setupEventHandlers() {
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveArtefak();
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
    
    // Save artefak data
    private void saveArtefak() {
        try {
            ArtefakDto artefakDto = createArtefakFromForm();
            
            if (currentArtefak != null && currentArtefak.getArtefakId() != null) {
                controller.updateArtefak(currentArtefak.getArtefakId(), artefakDto);
                JOptionPane.showMessageDialog(this, "Artefak berhasil diperbarui!");
            } else {
                controller.createArtefak(artefakDto);
                JOptionPane.showMessageDialog(this, "Artefak berhasil ditambahkan!");
            }
            
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Create ArtefakDto from form data
    private ArtefakDto createArtefakFromForm() {
        ArtefakDto dto = new ArtefakDto();
        dto.setNamaArtefak(namaField.getText().trim());
        dto.setDeskripsiArtefak(deskripsiArea.getText().trim());
        dto.setAsalDaerah(asalDaerahField.getText().trim());
        dto.setPeriode(periodeField.getText().trim());
        dto.setStatus(((StatusArtefak) statusCombo.getSelectedItem()).getDisplayName());
        dto.setGambar(gambarField.getText().trim());
        
        if (currentArtefak != null) {
            dto.setArtefakId(currentArtefak.getArtefakId());
            dto.setTanggalRegistrasi(currentArtefak.getTanggalRegistrasi());
        }
        
        return dto;
    }
    
    // Load artefak data for editing
    public void loadArtefak(ArtefakDto artefak) {
        this.currentArtefak = artefak;
        
        if (artefak != null) {
            namaField.setText(artefak.getNamaArtefak());
            deskripsiArea.setText(artefak.getDeskripsiArtefak());
            asalDaerahField.setText(artefak.getAsalDaerah());
            periodeField.setText(artefak.getPeriode());
            statusCombo.setSelectedItem(StatusArtefak.fromString(artefak.getStatus()));
            gambarField.setText(artefak.getGambar());
            setTitle("Edit Artefak");
        } else {
            clearForm();
            setTitle("Tambah Artefak Baru");
        }
    }
    
    // Clear form fields
    private void clearForm() {
        namaField.setText("");
        deskripsiArea.setText("");
        asalDaerahField.setText("");
        periodeField.setText("");
        statusCombo.setSelectedIndex(0);
        gambarField.setText("");
    }
}