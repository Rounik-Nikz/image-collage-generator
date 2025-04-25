# Glitch Collage Generator

This Java program creates a glitch-style collage by randomly sampling fragments from a dataset of images and arranging them on a canvas. The result is an abstract, fragmented composition that resembles digital glitch art.

## How It Works

### 1. Image Loading
The program first loads all PNG images from a specified folder (`small-images-data`):

```java
private static BufferedImage[] loadDatasetImages(File folder) {
    List<BufferedImage> imagesList = new ArrayList<>();
    File[] listOfFiles = folder.listFiles();

    if (listOfFiles != null) {
        for (File file : listOfFiles) {
            if (file.isFile() && file.getName().toLowerCase().endsWith(".png")) {
                try {
                    BufferedImage img = ImageIO.read(file);
                    if (img != null) {
                        imagesList.add(img);
                    }
                } catch (IOException e) {
                    System.err.println("⚠️ Error loading image: " + file.getAbsolutePath());
                }
            }
        }
    }
    return imagesList.toArray(new BufferedImage[0]);
}
```

Key points:
- Uses `ImageIO.read()` to load each PNG file
- Handles potential errors gracefully with try-catch blocks
- Returns an array of `BufferedImage` objects for processing

### 2. Canvas Preparation
Creates a blank canvas with specified dimensions (1024x1024 by default):

```java
int canvasWidth = 1024;
int canvasHeight = 1024;
BufferedImage collage = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_RGB);
Graphics2D g2d = collage.createGraphics();
g2d.setColor(Color.BLACK);
g2d.fillRect(0, 0, canvasWidth, canvasHeight); // Optional background
```

### 3. Fragment Generation and Placement
The core algorithm randomly selects image fragments and places them on the canvas:

```java
Random rand = new Random();
int numberOfFragments = 2000;  // Total fragments to place
int fragmentSize = 32;         // Size of each square fragment

for (int i = 0; i < numberOfFragments; i++) {
    // Select random source image
    BufferedImage img = datasetImages[rand.nextInt(datasetImages.length)];
    
    // Get random crop from source image
    int srcX = rand.nextInt(img.getWidth() - fragmentSize);
    int srcY = rand.nextInt(img.getHeight() - fragmentSize);
    BufferedImage chunk = img.getSubimage(srcX, srcY, fragmentSize, fragmentSize);
    
    // Place at random position on canvas
    int destX = rand.nextInt(canvasWidth - fragmentSize);
    int destY = rand.nextInt(canvasHeight - fragmentSize);
    
    g2d.drawImage(chunk, destX, destY, null);
}
```

### 4. Output
Finally, the generated collage is saved as a PNG file:

```java
File outputfile = new File("glitch_collage.png");
ImageIO.write(collage, "png", outputfile);
```

## Technical Details

### Key Classes Used
- `BufferedImage`: Stores and manipulates image data
- `Graphics2D`: Provides drawing capabilities
- `ImageIO`: Handles image reading/writing
- `Random`: Generates random numbers for fragment selection and placement

### Algorithm Complexity
The algorithm has linear complexity O(n) where n is the number of fragments, making it efficient even for large numbers of fragments.

### Parameters to Tweak
For different visual effects, adjust:
- `numberOfFragments`: More fragments create denser collages
- `fragmentSize`: Larger fragments make the source images more recognizable
- `canvasWidth`/`canvasHeight`: Changes the output dimensions
- Background color: Modify the `g2d.setColor()` call

## Example Output

The generated collage will look like a fragmented, glitchy composition where you can see hints of the original images but in a abstracted form. The randomness ensures each run produces a unique result.

## Extending the Project

Potential enhancements:
1. Add color filtering to fragments
2. Implement overlapping fragments with transparency
3. Add geometric transformations (rotation, scaling)
4. Create animated versions by generating sequences
5. Add user interface for parameter control

## Requirements

- Java 8 or higher
- A folder named `small-images-data` containing PNG images
- No external dependencies beyond standard Java libraries

## Usage

1. Place your source images in a `small-images-data` folder
2. Compile and run the program
3. Find the output in `glitch_collage.png`

```bash
javac GlitchCollageGenerator.java
java GlitchCollageGenerator
```

This project demonstrates fundamental image processing techniques in Java while creating visually interesting glitch art. The simple yet effective algorithm shows how randomness can be used creatively in digital art generation.