import string

def counter(text):
    count = 0
    text = text.translate(str.maketrans('', '', string.punctuation))
    
    # looping through each word in the string
    for word in text.split():
        # if word is equal to its reverse then it is palindrome
        if word == word[::-1] and len(word) > 2 and word.isalpha():
            count+=1  # add word to list

    return count


