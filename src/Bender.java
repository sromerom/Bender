import java.util.*;

class Bender {
    private Mapa mapa;
    Map<Coordenades, Vector> mapeig = new HashMap<Coordenades, Vector>();
    //List<HashMap<Coordenades,Vector>> mapeig = new ArrayList<HashMap<Coordenades, Vector>>();

    Coordenades[] prioritatActual = new Coordenades[4];

    public enum Coordenades {
        S, E, N, W
    }

    //Coordenades [] prioritatActual = new Coordenades[4];
    // Constructor: ens passen el mapa en forma d'String
    public Bender(String mapa) {
        this.mapa = new Mapa(mapa);
        //mapeig.add((HashMap<Coordenades, Vector>) new HashMap().put(Coordenades.S, new Vector(1, 0)));
        //mapeig.add((HashMap<Coordenades, Vector>) new HashMap().put(Coordenades.E, new Vector(0, 1)));
        //mapeig.add((HashMap<Coordenades, Vector>) new HashMap().put(Coordenades.N, new Vector(-1, 0)));
        //mapeig.add((HashMap<Coordenades, Vector>) new HashMap().put(Coordenades.W, new Vector(0, -1)));
        //System.out.println("Esto enseña un mapa " + mapeig.get(0));
        mapeig.put(Coordenades.S, new Vector(1, 0));
        mapeig.put(Coordenades.E, new Vector(0, 1));
        mapeig.put(Coordenades.N, new Vector(-1, 0));
        mapeig.put(Coordenades.W, new Vector(0, -1));
        prioritatActual[0] = Coordenades.S;
        prioritatActual[1] = Coordenades.E;
        prioritatActual[2] = Coordenades.N;
        prioritatActual[3] = Coordenades.W;
    }

    // Navegar fins a l'objectiu («$»).
    // El valor retornat pel mètode consisteix en una cadena de
    // caràcters on cada lletra pot tenir
    // els valors «S», «N», «W» o «E»,
    // segons la posició del robot a cada moment.
    public String run() {
        System.out.println("Supuesto mapa: " + this.mapa);
        //String resultat = "";
        Mapa mapaActualitzat = new Mapa(mapa.getMapaString());
        Coordenades direccioBender = Coordenades.S;
        int numeroPosicio = 0;
        int maximRecorregutX = 0;
        int maximRecorregutY = 0;
        int anteriorX = 0;
        int anteriorY = 0;
        int teleportadorsAgafats = 0;
        boolean shaMogut = false;
        String resultat = "";

        System.out.println("Posicio inicial Bender " + mapaActualitzat.getPosicioBender());
        System.out.println("Posicio objectiu " + mapaActualitzat.getObjectiu());
        System.out.println("-----------------");

        //System.out.println(mapeig.get(Direccions.SOUTH));
        while (!mapaActualitzat.getPosicioBender().equals(mapaActualitzat.getObjectiu())) {
            System.out.println("Posicio actual bender: " + mapaActualitzat.getPosicioBender());



            if (resultat.length() > 2 && shaMogut) {
                System.out.println("Y actual: " + mapaActualitzat.getPosicioBender().getY());
                System.out.println("Y anterior: " + anteriorY);
                if (anteriorY == mapaActualitzat.getPosicioBender().getY()) {
                    maximRecorregutY++;
                } else {
                    maximRecorregutY = 0;
                }


                if (anteriorX == mapaActualitzat.getPosicioBender().getX()) {
                    maximRecorregutX++;
                } else {
                    maximRecorregutX = 0;
                }
            }

            if (maximRecorregutX > (mapaActualitzat.getMapa().length * 2) && maximRecorregutX > (mapaActualitzat.getMapa()[0].length * 2)
            || maximRecorregutY > (mapaActualitzat.getMapa().length * 2) && maximRecorregutY > (mapaActualitzat.getMapa()[0].length * 2)) {
                return null;
            }

            if (teleportadorsAgafats > mapaActualitzat.getMapa().length * 2) {
                return null;
            }


            char objecteSeguent = mapaActualitzat.getMapa()[mapaActualitzat.getPosicioBender().opera(mapeig.get(direccioBender)).getX()][mapaActualitzat.getPosicioBender().opera(mapeig.get(direccioBender)).getY()];
            System.out.println("Objeto siguiente: " + objecteSeguent);
            if (objecteSeguent == ' ' || objecteSeguent == '$' || objecteSeguent == 'X') {
                resultat = resultat + direccioBender.toString();

                anteriorX = mapaActualitzat.getPosicioBender().getX();
                anteriorY = mapaActualitzat.getPosicioBender().getY();
                mapaActualitzat.setPosicioBender(mapaActualitzat.getPosicioBender().opera(mapeig.get(direccioBender)));
                shaMogut = true;
                System.out.println(mapaActualitzat.getPosicioBender());
                numeroPosicio = 0;

            } else if (objecteSeguent == '#') {

                if (numeroPosicio == 4) {
                    return null;
                }
                System.out.println(Arrays.toString(prioritatActual));
                //direccioBender = Coordenades.values()[numeroPosicio];
                direccioBender = prioritatActual[numeroPosicio];
                numeroPosicio++;
                shaMogut = false;
            } else if (objecteSeguent == 'T') {
                teleportadorsAgafats++;
                resultat += direccioBender.toString();
                Iterator<Vector> itr = mapaActualitzat.getTeleportadors().iterator();
                int selector = 0;
                int aux = 0;
                double distanciaActual = 0;
                double distanciaMinima = 0;

                mapaActualitzat.setPosicioBender(mapaActualitzat.getPosicioBender().opera(mapeig.get(direccioBender)));
                while (itr.hasNext()) {
                    Vector actual = itr.next();
                    System.out.println("Teleportador actual: " + actual);
                    if (!actual.equals(new Vector(mapaActualitzat.getPosicioBender().getX(), mapaActualitzat.getPosicioBender().getY()))) {
                        distanciaActual = actual.distanciaEuclidiana(mapaActualitzat.getPosicioBender());
                        if (distanciaMinima == 0) { //6,4
                            distanciaMinima = distanciaActual;
                            selector = aux;
                        }

                        if (distanciaActual == distanciaMinima) {
                            System.out.println("Estan a la misma distancia!!!!");

                            if (actual.getX() >= mapaActualitzat.getTeleportadors().get(selector).getX()) {
                                if (actual.getY())
                            }
                        } else if(distanciaActual < distanciaMinima) {
                            distanciaMinima = distanciaActual;
                            selector = aux;
                        }
                    }
                    aux++;
                }

                System.out.println("es transportara al següent punt: " + mapaActualitzat.getTeleportadors().get(selector));
                mapaActualitzat.setPosicioBender(mapaActualitzat.getTeleportadors().get(selector));
                shaMogut = true;
                //numeroPosicio = -1;
            } else if (objecteSeguent == 'I') {
                System.out.println(mapaActualitzat.getMapa()[mapaActualitzat.getPosicioBender().getX()][mapaActualitzat.getPosicioBender().getY()]);
                mapaActualitzat.getMapa()[mapaActualitzat.getPosicioBender().getX()][mapaActualitzat.getPosicioBender().getY()] = ' ';

                anteriorX = mapaActualitzat.getPosicioBender().getX();
                anteriorY = mapaActualitzat.getPosicioBender().getY();
                mapaActualitzat.setPosicioBender(mapaActualitzat.getPosicioBender().opera(mapeig.get(direccioBender)));
                resultat += direccioBender;
                Coordenades[] novaPrioritat = inverteix();
                System.arraycopy(novaPrioritat, 0, prioritatActual, 0, novaPrioritat.length);
                //direccioBender = prioritatActual[0];
                shaMogut = true;
            }
        }

        return resultat.toString();
    }

    public Coordenades[] inverteix() {
        Coordenades[] ar = new Coordenades[prioritatActual.length];
        Coordenades t = prioritatActual[2];
        Coordenades t2 = prioritatActual[3];
        ar[2] = prioritatActual[0];
        ar[3] = prioritatActual[1];

        ar[0] = t;
        ar[1] = t2;

        return ar;
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
        int altura = 0;
        int amplada = 0;
        int irregular = 0;


        /*
        while (mapa.charAt(amplada) == '#') {
            amplada++;
        }
        */

        int actualAmplada = 0;
        for (int i = 0; i < mapa.length(); i++) {
            if (mapa.charAt(i) != '\n') {
                actualAmplada++;
            } else {
                if (actualAmplada > amplada) {
                    amplada = actualAmplada;
                }
                actualAmplada = 0;
            }
        }

        for (int i = 0; i < mapa.length(); i++) {
            if (mapa.charAt(i) == '\n') {
                altura++;
            }
        }
        altura++;

        System.out.println("amplada: " + amplada);
        System.out.println("altura: " + altura);
        this.mapaString = mapa;
        this.mapa = new char[altura][amplada];

        omplirMapa();
    }



    /*
               if (actual == '\n') {
                   aux++;
                   nada = true;
                   continue;
               }
                */





    private void omplirMapa() {
        //System.out.println(this.mapaString.length());
        int aux = 0;
        boolean afegixAuto = false;
        for (int i = 0; i < this.getMapa().length; i++) {
            if (!afegixAuto && i != 0) {
                aux++;
            }
            afegixAuto = false;
            for (int j = 0; j < this.getMapa()[i].length; j++) {
                if (aux == this.mapaString.length()) {
                    break;
                }
                char actual = this.mapaString.charAt(aux);

                if (actual == '\n') {
                    aux++;
                    afegixAuto = true;
                    continue;
                }

                if (afegixAuto) {
                    continue;
                }
                /*
                if (actual == ' ') {
                    actual = '€';
                }

                 */

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
        }
        System.out.println(Arrays.deepToString(this.mapa));
        System.out.println();
    }

    @Override
    public String toString() {
        String res = "";
        //return mapaString;
        for (int i = 0; i < this.mapa.length; i++) {
            res += "\n";
            for (int j = 0; j < this.mapa[i].length; j++) {
               res += this.mapa[i][j];
            }
        }
        return res;
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

    public double distanciaEuclidiana(Vector v) {
        return Math.sqrt(Math.pow((v.x - this.x), 2) + Math.pow((v.y - this.y), 2));
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