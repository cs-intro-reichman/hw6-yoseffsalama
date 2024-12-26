import java.awt.Color;

/** A library of image processing functions. */
public class Runigram {

	public static void main(String[] args) {
	    
		//// Hide / change / add to the testing code below, as needed.
		
		// Tests the reading and printing of an image:	
		Color[][] tinypic = read("tinypic.ppm");
		print(tinypic);

		// Creates an image which will be the result of various 
		// image processing operations:
		Color[][] image;

		// Tests the horizontal flipping of an image:
		image = flippedHorizontally(tinypic);
		System.out.println();
		print(image);
		
		//// Write here whatever code you need in order to test your work.
		//// You can continue using the image array.
	}

	/** Returns a 2D array of Color values, representing the image data
	 * stored in the given PPM file. */
	public static Color[][] read(String fileName) {
		In in = new In(fileName);
		// Reads the file header, ignoring the first and the third lines.
		in.readString();
		int columns = in.readInt();
		int rows = in.readInt();
		in.readInt();
		// Creates the image array
		Color[][] image = new Color[rows][columns];
		// Reads the RGB values from the file into the image array. 
		// For each pixel (i,j), reads 3 values from the file,
		// creates from the 3 colors a new Color object, and 
		// makes pixel (i,j) refer to that object.
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				int red = in.readInt();
				int green = in.readInt();
				int blue = in.readInt();
				image[i][j] = new Color(red, green, blue);
			}
		}
		
		return image;
	}

    // Prints the RGB values of a given color.
	private static void print(Color c) {
	    System.out.print("(");
		System.out.printf("%3s,", c.getRed());   // Prints the red component
		System.out.printf("%3s,", c.getGreen()); // Prints the green component
        System.out.printf("%3s",  c.getBlue());  // Prints the blue component
        System.out.print(")  ");
	}

	// Prints the pixels of the given image.
	// Each pixel is printed as a triplet of (r,g,b) values.
	// This function is used for debugging purposes.
	// For example, to check that some image processing function works correctly,
	// we can apply the function and then use this function to print the resulting image.
	private static void print(Color[][] image) {
		//// Replace this comment with your code
		//// Notice that all you have to so is print every element (i,j) of the array using the print(Color) function.
	}
	
	/**
	 * Returns an image which is the horizontally flipped version of the given image. 
	 */
	public static Color[][] flippedHorizontally(Color[][] image) {
        int columns = image[0].length;
		int rows = image.length;
		Color[][] flipped = new Color[rows][columns];
		
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				flipped[i][j] = image[i][columns - 1 - j];
			}
		}
		
		return flipped;
	}
	
	/**
	 * Returns an image which is the vertically flipped version of the given image. 
	 */
	public static Color[][] flippedVertically(Color[][] image){
		int rows = image.length;
		int columns = image[0].length;
		Color[][] flipped = new Color[rows][columns];
		
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				flipped[i][j] = image[rows - 1 - i][j];
			}
		}
		
		return flipped;
	}
	
	// Computes the luminance of the RGB values of the given pixel, using the formula 
	// lum = 0.299 * r + 0.587 * g + 0.114 * b, and returns a Color object consisting
	// the three values r = lum, g = lum, b = lum.
	private static Color luminance(Color pixel) {
		int r = pixel.getRed();
		int g = pixel.getGreen();
		int b = pixel.getBlue();
		int lum = (int) (0.299 * r + 0.587 * g + 0.114 * b);
		lum = Math.max(0, Math.min(255, lum));
		return new Color(lum, lum, lum);
	}
	
	
	/**
	 * Returns an image which is the grayscaled version of the given image.
	 */
	public static Color[][] grayScaled(Color[][] image) {
		int rows = image.length;
		int columns = image[0].length;
		Color[][] grayImage = new Color[rows][columns];
	
		// Iterate over all pixels and apply the grayscale conversion
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				// Apply luminance to each pixel
				grayImage[i][j] = luminance(image[i][j]);
			}
		}
	
		return grayImage;
	}

	/**
	 * Returns an image which is the scaled version of the given image. 
	 * The image is scaled (resized) to have the given width and height.
	 */
	public static Color[][] scaled(Color[][] image, int width, int height) {
		int heightoriginal = image.length;
		int widthoriginal = image[0].length;
		Color[][] scaledImage = new Color[height][width];
		double rowScale = (double) heightoriginal / height;
		double colScale = (double) widthoriginal / width;
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int roworiginal = (int) (i * rowScale);
				int coloriginal = (int) (j * colScale);
				scaledImage[i][j] = image[roworiginal][coloriginal];
			}
		}
		
		return scaledImage;
	}
	
	/**
	 * Computes and returns a blended color which is a linear combination of the two given
	 * colors. Each r, g, b, value v in the returned color is calculated using the formula 
	 * v = alpha * v1 + (1 - alpha) * v2, where v1 and v2 are the corresponding r, g, b
	 * values in the two input color.
	 */
	public static Color blend(Color c1, Color c2, double alpha) {
		if (alpha < 0.0 || alpha > 1.0) {
			throw new IllegalArgumentException("Alpha must be between 0 and 1");
		}
		int red = (int) (alpha * c1.getRed() + (1 - alpha) * c2.getRed());
		int green = (int) (alpha * c1.getGreen() + (1 - alpha) * c2.getGreen());
		int blue = (int) (alpha * c1.getBlue() + (1 - alpha) * c2.getBlue());
		red = Math.min(255, Math.max(0, red));
		green = Math.min(255, Math.max(0, green));
		blue = Math.min(255, Math.max(0, blue));
		return new Color(red, green, blue);
	}
	
	/**
	 * Cosntructs and returns an image which is the blending of the two given images.
	 * The blended image is the linear combination of (alpha) part of the first image
	 * and (1 - alpha) part the second image.
	 * The two images must have the same dimensions.
	 */
	public static Color[][] blend(Color[][] image1, Color[][] image2, double alpha) {
		if (alpha < 0.0 || alpha > 1.0) {
			throw new IllegalArgumentException("Alpha must be between 0 and 1");
		}
		int height = image1.length;
		int width = image1[0].length;
		if (image2.length != height || image2[0].length != width) {
			throw new IllegalArgumentException("Both images must have the same dimensions.");
		}
		Color[][] blendedImage = new Color[height][width];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				blendedImage[i][j] = blend(image1[i][j], image2[i][j], alpha);
			}
		}
		return blendedImage;
	}

	/**
	 * Morphs the source image into the target image, gradually, in n steps.
	 * Animates the morphing process by displaying the morphed image in each step.
	 * Before starting the process, scales the target image to the dimensions
	 * of the source image.
	 */
	public static void morph(Color[][] source, Color[][] target, int n) {
		// Step 1: Resize the target image to match the dimensions of the source image
		target = scaled(target, source[0].length, source.length);
	
		// Step 2: Animate the morphing process
		for (int step = 0; step < n; step++) {
			// Calculate the interpolation factor (t) for this step
			double t = (double) step / (n - 1);
	
			// Create a new image to store the morphed result
			Color[][] morphedImage = new Color[source.length][source[0].length];
	
			// Step 3: Interpolate each pixel between the source and target images
			for (int i = 0; i < source.length; i++) {
				for (int j = 0; j < source[i].length; j++) {
					// Interpolate the RGB values for each pixel
					int r = (int) ((1 - t) * source[i][j].getRed() + t * target[i][j].getRed());
					int g = (int) ((1 - t) * source[i][j].getGreen() + t * target[i][j].getGreen());
					int b = (int) ((1 - t) * source[i][j].getBlue() + t * target[i][j].getBlue());
	
					// Ensure the RGB values are within valid bounds (0-255)
					r = Math.min(Math.max(r, 0), 255);
					g = Math.min(Math.max(g, 0), 255);
					b = Math.min(Math.max(b, 0), 255);
	
					// Store the morphed color in the new image
					morphedImage[i][j] = new Color(r, g, b);
				}
			}
	
			// Step 4: Display the morphed image using StdDraw
			display(morphedImage);
	
			// Optionally, add a delay to make the animation visible
			try {
				Thread.sleep(100); // Adjust the speed of the animation
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/** Creates a canvas for the given image. */
	public static void setCanvas(Color[][] image) {
		StdDraw.setTitle("Runigram 2023");
		int height = image.length;
		int width = image[0].length;
		StdDraw.setCanvasSize(width, height);
		StdDraw.setXscale(0, width);
		StdDraw.setYscale(0, height);
        // Enables drawing graphics in memory and showing it on the screen only when
		// the StdDraw.show function is called.
		StdDraw.enableDoubleBuffering();
	}

	/** Displays the given image on the current canvas. */
	public static void display(Color[][] image) {
		int height = image.length;
		int width = image[0].length;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				// Sets the pen color to the pixel color
				StdDraw.setPenColor( image[i][j].getRed(),
					                 image[i][j].getGreen(),
					                 image[i][j].getBlue() );
				// Draws the pixel as a filled square of size 1
				StdDraw.filledSquare(j + 0.5, height - i - 0.5, 0.5);
			}
		}
		StdDraw.show();
	}
}
