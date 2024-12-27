# McMaster Engineering Competition 2024, Programming Category
## Team Name: HATS
## Contributors and area of contributions:
1. Tridib Banik: Back-end
2. Shamil Canbolat: Front-end
3. Aidan Heathfield: Front-end
4. Harrison Johns: Back-end

## I, Tridib Banik, contributed to the PasswordGenerator class of this project:
- hashPassword() method that converts a plain-text password, hashes it using the MD5 algorithm, and returns the hash as a hexadecimal string.
- toHexString() method that converts a byte array to a hexadecimal String.
- encryptPassword512() method that converts the hexadecimal String to 512-bit hash using the SHA-512 algorithm that ensures fixed output size for variable inputs. The hash can’t be reversed to retrieve the original input which ensures security.


## How to Run App
1. In the root directory of project run this line in the command line `cd MEC24`
2. Then in command line run these 3 commands

```
mvn compile
mvn package
java -cp target/MEC24-1.0.0.jar HATS.App
```

## How to Test App
1. In the "Convert Image to Password" Tab upload a any photo
2. Click "Add to Vault" button at the bottom
3. Fill in popup and click "Add" button
4. You can check out the "Passcode" information in the Password Vault tab and double click the image you just put in.
5. A popup will come up and if you want you can copy the password there
6. Go to the "Test" tab
7. Either paste that password in or input the same image, and click "Submit" button
8. A success or failure popup will appear letting you know if the image or password matched
- IMPORTANT NOTE: The test works by setting the correct password and image to the most recent one added from the vault
  - The "Test" tab is just there to showcase its functionality
