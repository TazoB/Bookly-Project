package Model;

import java.sql.Timestamp;

public class Character {
    private int id;
    private String name;
    private String alias;
    private String characterType;
    private String gender;
    private int age;
    private String description;
    private String importance;
    private String death;
    private String physicalAppearance;
    private String relateToMainChar;
    private Timestamp dateCreated;

    public Character(int id, String name, String alias, String characterType, String gender, int age, String description, String importance, String death, String physicalAppearance, String relateToMainChar, Timestamp dateCreated) {
        this.id = id;
        this.name = name;
        this.alias = alias;
        this.characterType = characterType;
        this.gender = gender;
        this.age = age;
        this.description = description;
        this.importance = importance;
        this.death = death;
        this.physicalAppearance = physicalAppearance;
        this.relateToMainChar = relateToMainChar;
        this.dateCreated = dateCreated;
    }

    public Character(String name, String alias, String characterType, String gender, int age, String description, String importance, String death, String physicalAppearance, String relateToMainChar, Timestamp dateCreated) {
        this.name = name;
        this.alias = alias;
        this.characterType = characterType;
        this.gender = gender;
        this.age = age;
        this.description = description;
        this.importance = importance;
        this.death = death;
        this.physicalAppearance = physicalAppearance;
        this.relateToMainChar = relateToMainChar;
        this.dateCreated = dateCreated;
    }

    public Character(String name, String alias, String characterType, String gender, int age, String description, String importance, String death, String physicalAppearance, String relateToMainChar) {
        this.name = name;
        this.alias = alias;
        this.characterType = characterType;
        this.gender = gender;
        this.age = age;
        this.description = description;
        this.importance = importance;
        this.death = death;
        this.physicalAppearance = physicalAppearance;
        this.relateToMainChar = relateToMainChar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getCharacterType() {
        return characterType;
    }

    public void setCharacterType(String characterType) {
        this.characterType = characterType;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImportance() {
        return importance;
    }

    public void setImportance(String importance) {
        this.importance = importance;
    }

    public String getDeath() {
        return death;
    }

    public void setDeath(String death) {
        this.death = death;
    }

    public String getPhysicalAppearance() {
        return physicalAppearance;
    }

    public void setPhysicalAppearance(String physicalAppearance) {
        this.physicalAppearance = physicalAppearance;
    }

    public String getRelateToMainChar() {
        return relateToMainChar;
    }

    public void setRelateToMainChar(String relateToMainChar) {
        this.relateToMainChar = relateToMainChar;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }
}