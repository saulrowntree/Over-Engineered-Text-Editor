import unittest
import palindromecount

class TestCount(unittest.TestCase):

    def test_count(self):
        self.assertEqual(palindromecount.counter("mom racecar dog brown"),2)
        self.assertNotEqual(palindromecount.counter("mom dad"),3)
if __name__ == '__main__':
    unittest.main()


