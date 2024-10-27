package org.example.model;

import java.util.ArrayList;

/**
 * Сущность для структурирования студентов
 * @param name название группы
 * @param students совокупность студентов, учащихся в этой группе
 */
public record Group(String name, ArrayList<Student> students) {

}
