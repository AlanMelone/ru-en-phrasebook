package com;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

/**
 * Created by Admin on 25.11.2015.
 */
public class MyDaoGenerator {
    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(3, "com.r_mobile");
        createTables(schema);
        new DaoGenerator().generateAll(schema, "../PhaseBook/app/src/main/java");
    }

    private static void createTables(Schema schema) {
        Entity category = schema.addEntity("Category");
        category.setTableName("Categories");
        category.addIdProperty().primaryKey();
        category.addStringProperty("label").notNull();

        Entity phrase = schema.addEntity("Phrase");
        phrase.setTableName("Phrases");
        phrase.addIdProperty().primaryKey();
        phrase.addStringProperty("phrase");
        phrase.addIntProperty("favorite");
        phrase.addIntProperty("own");

        Entity translate = schema.addEntity("Translate");
        translate.setTableName("Translations");
        translate.addIdProperty();
        translate.addStringProperty("language");
        translate.addStringProperty("transcription");
        translate.addStringProperty("content");

        Property phraseCategoryID = phrase.addLongProperty("categoryId").getProperty();
        phrase.addToOne(category, phraseCategoryID);
        category.addToMany(phrase, phraseCategoryID);

        Property translatePhraseID = translate.addLongProperty("phraseId").getProperty();
        translate.addToOne(phrase, translatePhraseID);
        phrase.addToMany(translate, translatePhraseID);
    }
}
