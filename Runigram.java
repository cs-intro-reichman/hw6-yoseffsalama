import java.awt.Color;

/** A library of image processing functions. */
public class Runigram {

    public static void main(String[] args) {
        // Cargar y mostrar una imagen de prueba
        Color[][] tinypic = read("tinypic.ppm");
        print(tinypic);

        // Descomentar las siguientes líneas para probar otras imágenes
        // Color[][] eyes = read("eyes.ppm");
        // setCanvas(eyes);
        // display(eyes);
        // StdDraw.pause(1000);
        // setCanvas(scaled(eyes, 400, 800));
        // display(scaled(eyes, 400, 800));

        // Crear una imagen que será el resultado de varias operaciones de procesamiento de imágenes
        Color[][] image;

        // Probar la inversión horizontal de la imagen
        image = flippedHorizontally(tinypic);
        System.out.println();
        print(image);
        
        // Probar la conversión a escala de grises
        image = grayScaled(tinypic);
        System.out.println();
        print(image);

        // Probar el escalado de la imagen
        image = scaled(tinypic, 3, 5);
        System.out.println();
        print(image);
    }

    /** 
     * Lee un archivo PPM y devuelve un arreglo 2D de valores Color que representa los datos de la imagen. 
     */
    public static Color[][] read(String fileName) {
        In in = new In(fileName);
        in.readString(); // Leer el identificador del formato PPM
        int numCols = in.readInt(); // Leer el número de columnas
        int numRows = in.readInt(); // Leer el número de filas
        in.readInt(); // Leer el valor máximo de color (ignorado)

        // Crear el arreglo de imagen
        Color[][] image = new Color[numRows][numCols];

        // Leer los valores RGB del archivo y almacenarlos en el arreglo de imagen
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                int red = in.readInt();
                int green = in.readInt();
                int blue = in.readInt();
                image[i][j] = new Color(red, green, blue);
            }
        }
        return image;
    }

    // Imprime los valores RGB de un color dado.
    private static void print(Color c) {
        System.out.print("(");
        System.out.printf("%3s,", c.getRed());
        System.out.printf("%3s,", c.getGreen());
        System.out.printf("%3s", c.getBlue());
        System.out.print(")  ");
    }

    // Imprime los píxeles de la imagen dada.
    private static void print(Color[][] image) {
        for (Color[] row : image) {
            for (Color pixel : row) {
                print(pixel);
            }
            System.out.println();
        }
    }

    /** 
     * Devuelve una imagen que es la versión invertida horizontalmente de la imagen dada. 
     */
    public static Color[][] flippedHorizontally(Color[][] image) {
        int numRows = image.length;
        int numCols = image[0].length;
        Color[][] resImage = new Color[numRows][numCols];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                resImage[i][j] = image[i][numCols - j - 1];
            }
        }
        return resImage;
    }

    /** 
     * Devuelve una imagen que es la versión invertida verticalmente de la imagen dada. 
     */
    public static Color[][] flippedVertically(Color[][] image) {
        int numRows = image.length;
        Color[][] resImage = new Color[numRows][];
        for (int i = 0; i < numRows; i++) {
            resImage[i] = image[numRows - i - 1];
        }
        return resImage;
    }

    // Calcula la luminancia de los valores RGB de un píxel dado.
    private static Color luminance(Color pixel) {
        int r = pixel.getRed();
        int g = pixel.getGreen();
        int b = pixel.getBlue();
        int lum = (int) (0.299 * r + 0.587 * g + 0.114 * b);
        return new Color(lum, lum, lum);
    }

    /** 
     * Devuelve una imagen que es la versión en escala de grises de la imagen dada. 
     */
    public static Color[][] grayScaled(Color[][] image) {
        int numRows = image.length
