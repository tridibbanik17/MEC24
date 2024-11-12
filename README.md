# MEC24

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
