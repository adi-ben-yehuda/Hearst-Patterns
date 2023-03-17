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

### Part 1 - Construct a database of hypernym relations
The program will read all the files in the directory, find and aggregate hypernym relations that match the Hearst patterns using regular expressions, and save them in a txt file.
Hypernym relations are semantic relations between two noun phrases (NP) which can include multiple words. 
Although finding NPs in a text is a problem on its own, the database already contains all NPs in the text.
There are a lot of Hearst patterns, but I'll implement only a partial list.
1. <img width="250" alt="image" src="https://user-images.githubusercontent.com/75027826/225546578-bcb8afd8-bcaa-4b8d-89ed-a9c39f905f2d.png">

In this pattern, the first NP is the hypernym and the NPs after the words "such as" are hyponyms.
Example: "semitic languages such as Hebrew or Arabic are composed of consonants and voyels"
semitic language ⟶ Hebrew
semitic language ⟶ Arabic

2. <img width="226" alt="image" src="https://user-images.githubusercontent.com/75027826/225546813-e1b1aa29-6e2a-4ca1-9187-6120c2422ea7.png">

The first NP is the hypernym and the NPs after the words "as" are hyponyms.
Example: "courses taught by such lecturers as Hemi, Arie, and Hodyah are great"
lecturers ⟶ Hemi
lecturers ⟶ Arie
lecturers ⟶ Hodyah

3. <img width="253" alt="image" src="https://user-images.githubusercontent.com/75027826/225547103-17335e31-96e6-41c7-b7d3-c57d7cf38748.png">

The first NP is the hypernym and the NPs after the words "including" are hyponyms.

4. <img width="264" alt="image" src="https://user-images.githubusercontent.com/75027826/225547198-b9c2e939-928b-4050-b7b2-de16ed54347d.png">

The first NP is the hypernym and the NPs after the words "especially" are hyponyms.

5. <img width="296" alt="image" src="https://user-images.githubusercontent.com/75027826/225547407-6ec4ddae-2339-46fa-b0a9-fdbaa4e34a32.png">

The first NP is the hyponym and the second in a hypernym. Example: "Object oriented programming, which is an example of a computer science course".
I accept the following: (the "," is optionally)
NP {,} which is NP
NP {,} which is an example of NP
NP {,} which is a kind of NP
NP {,} which is a class of NP

I don't want to distinguish between "Animal --> cat" and "animal --> cat". Therefore, I lower all NPs before applying the regular expressions.

At the end of the process, I group the hyponyms of the same hypernyms (also called co-hyponyms), ignore hypernyms that have less than 3 distinct hyponyms and save the predicted relations in a file. If a file exists in the path provided as argument, my program will overwrite it, otherwise, I will create a new file at the given path. The format of the file should be as follows:

<img width="246" alt="image" src="https://user-images.githubusercontent.com/75027826/225548362-b7656f8b-4f1c-4673-9ee6-dec6f0bb9440.png">

where (x) corresponds to the number of occurrences of the relations (across all possible patterns) in the corpus. For each hypernym, I sort the list of co-hyponyms according to (x) in a descending order. If two hyponyms have the same number of relations, I sort them alphabetically, as follows:

<img width="305" alt="image" src="https://user-images.githubusercontent.com/75027826/225548565-e951eab6-8e4c-4889-b44c-8442fab2b8e2.png">

Additionally, the hypernyms should also be sorted alphabetically.

### Part 2 - Hypernym discovery
The main method will get 2 arguments: (1) the absolute path to the directory of the corpus and (2) a lemma.
The program will search all the possible hypernyms of the input lemma and print them to the console as follows.

<img width="100" alt="image" src="https://user-images.githubusercontent.com/75027826/225549075-9fdadecf-b7bb-4a2d-94c3-2a04ddb02e50.png">

where (x) corresponds to the number of occurrences of the relations (across all possible patterns) in the corpus. Like in Part 1, hypernyms need to be sorted in a descending order according to (x).

If the input lemma doesn't appear in the corpus, print: The lemma doesn't appear in the corpus.

## Installation
Before installing this project, you need to install on your computer:
* Git
* IDE such as Intellij, Eclipse, etc.
* JDK

Run the following command in the terminal:

```
git clone https://github.com/adi-ben-yehuda/Hearst-Patterns.git
```
Download the corpus from [here](https://drive.google.com/drive/folders/11aVnX9r-k5iy2GafZd-o5lBBgeNRuFDN)

There are two options to run this project. 
First, open the project using any IDE.
Create configuration with CreateHypernymDatabase as the Main Class, and add 2 agruments:
first- The directory path of the corpus.
second- name for the new txt file.
Run the project.

The second option is to use an ant.
Install [Apache Ant](https://ant.apache.org/bindownload.cgi)
Run the following command in the terminal:

```
ant compile
ant run1 (first argument- The directory of the corpes, Second argument- name for the new txt file)
ant run2 (first argument- The directory of the corpes, Second argument- a lemma)
```

## Contact
Created by @adi-ben-yehuda - feel free to contact me!
