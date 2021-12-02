package com.project.a_star_fitness;

public class User {
    public String email;
    public String name;
    public String expectation;
    public String gender;
    public String work;
    public int age;
    public int height;
    public int weight;
    public String score;

    public User() {

    }

    public User(String email) {

        this.email = email;
        this.name = "empty";
        this.expectation = "empty";
        this.gender = "empty";
        this.work = "empty";
        this.age = 0;
        this.height = 0;
        this.weight = 0;
        this.score = "empty";

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExpectation() {
        return expectation;
    }

    public void setExpectation(String expectation) {
        this.expectation = expectation;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
