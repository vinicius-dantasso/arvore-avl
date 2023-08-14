package com.vinicius.dao;

import java.io.FileWriter;
import java.io.IOException;

import com.vinicius.entities.Vehicle;

class Node {
    Vehicle vehicle;
    int height;
    Node left;
    Node right;

    public Node(Vehicle vehicle) {
        this.vehicle = vehicle;
        this.height = 0;
        this.left = null;
        this.right = null;
    }
}

public class AVLTree {
    private Node root;
    private int total;

    public AVLTree() {
        this.root = null;
        this.total = 0;
    }

    private int height(Node node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }

    private int balancingFactor(Node node) {
        if (node == null) {
            return 0;
        }
        return height(node.left) - height(node.right);
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y;
    }

    public void insert(Vehicle vehicle) {
        root = insert(root, vehicle);
    }

    private Node insert(Node node, Vehicle vehicle) {
        if (node == null) {
            fileWriter("Veículo Inserido!\n" + "Altura Atual da Árvore:" + (height(root)+1) + "\n");
            return new Node(vehicle);
        }

        String vehiclePlate = vehicle.getPlate();
        if (vehiclePlate.compareTo(node.vehicle.getPlate()) < 0) {
            node.left = insert(node.left, vehicle);
        } else if (vehiclePlate.compareTo(node.vehicle.getPlate()) > 0) {
            node.right = insert(node.right, vehicle);
        } else {
            // Veículos duplicados não são permitidos na árvore AVL
            System.out.println("\nVeículo já está cadastrado!\n");
            return node;
        }

        node.height = 1 + Math.max(height(node.left), height(node.right));
        int balanceamento = balancingFactor(node);

        // Caso Esquerda-Esquerda
        if (balanceamento > 1 && vehiclePlate.compareTo(node.left.vehicle.getPlate()) < 0) {
            fileWriter("Rotação Direita Simples\n");
            return rotateRight(node);
        }

        // Caso Direita-Direita
        if (balanceamento < -1 && vehiclePlate.compareTo(node.right.vehicle.getPlate()) > 0) {
            fileWriter("Rotação Esquerda Simples\n");
            return rotateLeft(node);
        }

        // Caso Esquerda-Direita
        if (balanceamento > 1 && vehiclePlate.compareTo(node.left.vehicle.getPlate()) > 0) {
            fileWriter("Rotação Dupla Direita\n");
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        // Caso Direita-Esquerda
        if (balanceamento < -1 && vehiclePlate.compareTo(node.right.vehicle.getPlate()) < 0) {
            fileWriter("Rotação Dupla Esquerda\n");
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    public void remove(String vehiclePlate) {
        root = remove(root, vehiclePlate);
    }

    private Node remove(Node node, String vehiclePlate) {
        if (node == null) {
            return node;
        }

        if (vehiclePlate.compareTo(node.vehicle.getPlate()) < 0) {
            node.left = remove(node.left, vehiclePlate);
        } else if (vehiclePlate.compareTo(node.vehicle.getPlate()) > 0) {
            node.right = remove(node.right, vehiclePlate);
        } else {
            // Nó com apenas um filho ou sem filhos
            if ((node.left == null) || (node.right == null)) {
                Node temp = null;
                if (node.left == null) {
                    temp = node.right;
                } else {
                    temp = node.left;
                }

                // Caso sem filhos
                if (temp == null) {
                    temp = node;
                    node = null;
                } else { // Caso com um filho
                    node = temp;
                }
            } else {
                // Nó com dois filhos: obter o sucessor in-order (menor valor na subárvore à direita)
                Node temp = findMin(node.right);
                node.vehicle = temp.vehicle;
                node.right = remove(node.right, temp.vehicle.getPlate());
            }
        }

        if (node == null) {
            return node;
        }

        // Atualizar a height do nó atual
        node.height = 1 + Math.max(height(node.left), height(node.right));

        // Obter o fator de balanceamento deste nó
        int balanceamento = balancingFactor(node);

        // Caso Esquerda-Esquerda
        if (balanceamento > 1 && balancingFactor(node.left) >= 0) {
            return rotateRight(node);
        }

        // Caso Direita-Direita
        if (balanceamento < -1 && balancingFactor(node.right) <= 0) {
            return rotateLeft(node);
        }

        // Caso Esquerda-Direita
        if (balanceamento > 1 && balancingFactor(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        // Caso Direita-Esquerda
        if (balanceamento < -1 && balancingFactor(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    private Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    public Node searchByPlate(String vehiclePlate) {
        Node node = searchByPlate(root, vehiclePlate);

        if(node != null){
            System.out.println(
            "\n// --------------------------------------" +
            "\nPlaca: " + node.vehicle.getPlate() +
            "\nRenavam: " + node.vehicle.getRenavam() +
            "\nModelo: " + node.vehicle.getModelName() +
            "\nData de Fabricação: " + node.vehicle.getFabricationDate() +
            "\nProprietário: " + node.vehicle.getConductor().getName() +
            "\nCPF: " + node.vehicle.getConductor().getCpf() +
            "\n// --------------------------------------\n"
            );
        }
        else{
            System.out.println("\nVeículo não encontrado!\n");
        }

        return node;
    }

    private Node searchByPlate(Node node, String vehiclePlate) {
        if (node == null || node.vehicle.getPlate().equals(vehiclePlate)) {
            return node;
        }

        if (vehiclePlate.compareTo(node.vehicle.getPlate()) < 0) {
            return searchByPlate(node.left, vehiclePlate);
        } else {
            return searchByPlate(node.right, vehiclePlate);
        }
    }

    public Node searchByRenavam(String vehicleRenavam) {
        Node node = searchByRenavam(root, vehicleRenavam);

        System.out.println(
            "\n// --------------------------------------" +
            "\nPlaca: " + node.vehicle.getPlate() +
            "\nRenavam: " + node.vehicle.getRenavam() +
            "\nModelo: " + node.vehicle.getModelName() +
            "\nData de Fabricação: " + node.vehicle.getFabricationDate() +
            "\nProprietário: " + node.vehicle.getConductor().getName() +
            "\nCPF: " + node.vehicle.getConductor().getCpf() +
            "\n// --------------------------------------\n"
        );

        return node;
    }

    private Node searchByRenavam(Node node, String vehicleRenavam) {
        if (node == null || node.vehicle.getRenavam().equals(vehicleRenavam)) {
            return node;
        }

        if (vehicleRenavam.compareTo(node.vehicle.getRenavam()) < 0) {
            return searchByRenavam(node.left, vehicleRenavam);
        } else {
            return searchByRenavam(node.right, vehicleRenavam);
        }
    }

    // Função para realizar uma travessia em ordem na árvore
    public void inOrderTraversal() {
        inOrderTraversal(root);
    }

    private void inOrderTraversal(Node node) {
        if (node != null) {
            inOrderTraversal(node.left);
            System.out.println(
                "\n// --------------------------------------" +
                "\nPlaca: " + node.vehicle.getPlate() +
                "\nRenavam: " + node.vehicle.getRenavam() +
                "\nModelo: " + node.vehicle.getModelName() +
                "\nData de Fabricação: " + node.vehicle.getFabricationDate() +
                "\nProprietário: " + node.vehicle.getConductor().getName() +
                "\nCPF: " + node.vehicle.getConductor().getCpf() +
                "\n// --------------------------------------\n"
            );
            inOrderTraversal(node.right);
        }
    }

    // Função para devolver o total de veículos na árvore
    public int totalVehicles(){
        return totalVehicles(root);
    }

    private int totalVehicles(Node node){
        if(node != null){
            totalVehicles(node.left);
            total++;
            totalVehicles(node.right);
        }
        return total;
    }

    public void setTotal(){
        this.total = 0;
    }

    // Função para atualizar os dados de um veículo na árvore
    public boolean updateVehicle(Vehicle vehicle){

        Node node = searchByPlate(vehicle.getPlate());
        if(node != null){
            // Remover o veículo desejado para alterar
            root = remove(root, node.vehicle.getPlate());

            // Inserir o novo veículo atualizado na árvore
            insert(vehicle);
            return true;
        }
        else{
            // Veículo não encontrado na árvore
            return false;
        }

    }

    // Função para retornar a altura de cada novo veículo inserido ou removido
    public int getHeight(String vehiclePlate){

        Node node = searchByPlate(vehiclePlate);
        return node.height-1;

    }

    private void fileWriter(String content){
        try{
            FileWriter writer = new FileWriter("Log.txt",true);
            writer.write(content);
            writer.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

}

