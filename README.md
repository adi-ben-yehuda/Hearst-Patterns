# Hearst-Patterns

## Introduction
Hypernymy (also called IS-A relation) is a semantic relation between two noun phrases, hypernym and hyponym, such that the hyponym is a subtype of the hypernym. For example, cat and dog are hyponym of animal because they are types of animals. Hypernym relations are hierarchical, so a word can have multiple hypernyms. For example, dog is a hyponym of animal, canine and mammal.
Many methods have been developed in the last decades in order to automatically construct a database of hypernym relations from large corpora.
A well-established approach to do so is using lexico-syntactic patterns, often called Hearst patterns (as the idea was introduced by Marti Hearst in this paper). For example, given the sentence "semitic languages such as Hebrew or Arabic are composed of consonants and voyels", we can infer that both Hebrew and Arabic are semitic languages. Such relations can be retrieved using a simple regular expression.


## Table of contents
* [General Information](#general-information)
* [Installation](#installation)
* [Contact](#Contact)

## General Information
There are two parts in this assignment:
1. Construct a database of hypernym relations
2. Hypernym discovery

# Part 1 - Construct a database of hypernym relations
The program will read all the files in the directory, find and aggregate hypernym relations that match the Hearst patterns using regular expressions, and save them in a txt file.
Hypernym relations are semantic relations between two noun phrases (NP) which can include multiple words. 
Although finding NPs in a text is a problem on its own, the database already contains all NPs in the text.
There are a lot of Hearst patterns, but I'll implement only a partial list.
1. <img width="250" alt="image" src="https://user-images.githubusercontent.com/75027826/225546578-bcb8afd8-bcaa-4b8d-89ed-a9c39f905f2d.png">
In this pattern, the first NP is the hypernym and the NPs after the words "such as" are hyponyms.
Example: "semitic languages such as Hebrew or Arabic are composed of consonants and voyels"
semitic language ⟶ Hebrew
semitic language ⟶ Arabic

## Installation
Before installing this project, you need to install on your computer:
* Git

After it, run the following commands in the terminal:

```
git clone https://github.com/adi-ben-yehuda/Game.git
ant compile
ant -Dargs="{text}" run
```
The text will contain the levels that will be shown in the game. There are four levels. If the user enters different numbers or letters, the program will ignore it. 

## Contact
Created by @adi-ben-yehuda - feel free to contact me!
