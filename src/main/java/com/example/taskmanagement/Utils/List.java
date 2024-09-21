package com.example.taskmanagement.Utils;

public class List {

    public class node{
        private User user;
        private node next;

        public node(User user) {
            this.user = user;
            this.next = null;
        }
    }
    node first = null;

    public void add(User user){
        node n = new node(user);
        if (first == null){
            first = n;
        }
        else{
            n.next = first;
            first = n;
        }
    }
    public boolean contains(User user) {
        node temp = first;
        while (temp != null) {
            if (temp.user.getUsername().equals(user.getUsername()) &&
                    temp.user.getPassword().equals(user.getPassword())) {
                return true;
            }
            temp = temp.next;
        }
        return false;
    }
}
