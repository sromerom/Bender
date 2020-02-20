import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Bender {
    private Mapa mapa;
    Map<Direccions, Vector> mapeig = new HashMap<Direccions, Vector>();

    public enum Direccions {
        S, E, N, W
    }

    // Constructor: ens passen el mapa en forma d'String
    public Bender(String mapa) {
        this.mapa = new Mapa(mapa);
        mapeig.put(Direccions.S, new Vector(1, 0));
        mapeig.put(Direccions.E, new Vector(0, 1));
        mapeig.put(Direccions.N, new Vector(-1, 0));
        mapeig.put(Direccions.W, new Vector(0, -1));
    }

    // Navegar fins a l'objectiu («$»).
    // El valor retornat pel mètode consisteix en una cadena de
    // caràcters on cada lletra pot tenir
    // els valors «S», «N», «W» o «E»,
    // segons la posició del robot a cada moment.
    public String run() {
        String resultat = "";
        Mapa mapaActualitzat = new Mapa(mapa.getMapaString());
        Direccions direccioBender = Direccions.S;
        int numeroPosicio = 0;
        System.out.println("Posicio inicial Bender " + mapaActualitzat.getPosicioBender());
        System.out.println("Posicio objectiu " + mapaActualitzat.getObjectiu());


        //System.out.println(mapeig.get(Direccions.SOUTH));
        while (!mapaActualitzat.getPosicioBender().equals(mapaActualitzat.getObjectiu())) {
            if (mapaActualitzat.getMapa()[mapaActualitzat.getPosicioBender().opera(mapeig.get(direccioBender)).getX()][mapaActualitzat.getPosicioBender().opera(mapeig.get(direccioBender)).getY()] != '#') {
                resultat = resultat + direccioBender.toString();
                mapaActualitzat.setPosicioBender(mapaActualitzat.getPosicioBender().opera(mapeig.get(direccioBender)));
                System.out.println(mapaActualitzat.getPosicioBender());

            } else {
                numeroPosicio++;
                direccioBender = Direccions.values()[numeroPosicio];
            }
        }

        return resultat.toString();
    }

    public String bestRun() {
        return "";
    }
}

class Mapa {
    private String mapaString;
    private char[][] mapa;
    private Vector posicioBender;
    private Vector objectiu;
    private List<Vector> inversors = new ArrayList<>();
    private List<Vector> teleportadors = new ArrayList<>();

    public Mapa(String mapa) {
        int totalCaracters = mapa.length();
        int altura = 1;
        int amplada = 0;


        while (mapa.charAt(amplada) == '#') {
            amplada++;
        }

        while (amplada * altura < totalCaracters) {
            altura++;
        }
        altura -= 1;

        this.mapaString = mapa;
        this.mapa = new char[altura][amplada];

        omplirMapa();
    }

    private void omplirMapa() {
        int aux = 0;
        for (int i = 0; i < this.getMapa().length; i++) {
            for (int j = 0; j < this.getMapa()[i].length; j++) {
                char actual = this.mapaString.charAt(aux);
                if (actual == 'X') {
                    this.setPosicioBender(new Vector(i, j));
                } else if (actual == 'I') {
                    this.getInversors().add(new Vector(i, j));
                } else if (actual == 'T') {
                    this.getTeleportadors().add(new Vector(i, j));
                } else if (actual == '$') {
                    this.setObjectiu(new Vector(i, j));
                }

                this.mapa[i][j] = actual;
                aux++;
            }
            aux++;
        }
    }

    @Override
    public String toString() {
        return mapaString;
    }

    public String getMapaString() {
        return mapaString;
    }

    public void setMapaString(String mapaString) {
        this.mapaString = mapaString;
    }

    public char[][] getMapa() {
        return mapa;
    }

    public void setMapa(char[][] mapa) {
        this.mapa = mapa;
    }

    public Vector getPosicioBender() {
        return posicioBender;
    }

    public void setPosicioBender(Vector posicioBender) {
        this.posicioBender = posicioBender;
    }

    public Vector getObjectiu() {
        return objectiu;
    }

    public void setObjectiu(Vector objectiu) {
        this.objectiu = objectiu;
    }

    public List<Vector> getInversors() {
        return inversors;
    }

    public void setInversors(List<Vector> inversors) {
        this.inversors = inversors;
    }

    public List<Vector> getTeleportadors() {
        return teleportadors;
    }

    public void setTeleportadors(List<Vector> teleportadors) {
        this.teleportadors = teleportadors;
    }
}

class Vector {
    private int x = 0;
    private int y = 0;

    public Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector opera(Vector v) {
        return new Vector(this.getX() + v.getX(), this.getY() + v.getY());
    }

    @Override
    public String toString() {
        return x + "," + y;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vector) {
            Vector v = (Vector) obj;
            if (this.x == v.x && this.y == v.y) return true;
        }
        return false;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }
}