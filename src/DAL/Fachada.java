/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAL;

import Mundo.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.persistence.Persistence;
import javax.swing.ImageIcon;

/**
 *
 * @author Santiago Caro
 */
public class Fachada {

    public List<Partidos> obtenerPartidos() {
        List<Partidos> p = null;
        PartidosJpaController pc = new PartidosJpaController(Persistence.createEntityManagerFactory("BasesFinalPU"));
        p = pc.findPartidosEntities();
        return p;
    }

    public Partidos obtenerPartido(BigInteger i) {
        Partidos p = null;
        PartidosJpaController pc = new PartidosJpaController(Persistence.createEntityManagerFactory("BasesFinalPU"));
        p = pc.findPartidos(i);
        return p;
    }

    public long obtenerCantiDis(Partidos p, BigInteger c) {
        long r = 0;
        BoleteriaJpaController pc = new BoleteriaJpaController(Persistence.createEntityManagerFactory("BasesFinalPU"));
        r = pc.obtenerCantiDis(p, c);
        return r;

    }

    public long obtenerCanti(Partidos p) {
        long r = 0;
        BoleteriaJpaController pc = new BoleteriaJpaController(Persistence.createEntityManagerFactory("BasesFinalPU"));
        r = pc.obtenerCanti(p);
        return r;
    }

    public long obtenerCantiPat(Partidos p, BigInteger c) {
        long r = 0;
        BoleteriaJpaController pc = new BoleteriaJpaController(Persistence.createEntityManagerFactory("BasesFinalPU"));
        r = pc.obtenerCantiPat(p, c);
        return r;
    }

    public List<Boleteria> obtenerCantPat(Partidos p, BigInteger c) {
        List<Boleteria> r;
        BoleteriaJpaController pc = new BoleteriaJpaController(Persistence.createEntityManagerFactory("BasesFinalPU"));
        r = pc.obtenerCantPat(p, c);
        return r;
    }

    public List<Boleteria> obtenerCantDis(Partidos p, BigInteger c) {
        List<Boleteria> r;
        BoleteriaJpaController pc = new BoleteriaJpaController(Persistence.createEntityManagerFactory("BasesFinalPU"));
        r = pc.obtenerCantDis1(p, c);
        return r;
    }

    public long obtenerDisponbles(Partidos p) {
        long r = 0;
        BoleteriaJpaController pc = new BoleteriaJpaController(Persistence.createEntityManagerFactory("BasesFinalPU"));
        r = pc.getDisponibles(p);
        return r;
    }

    public Categorias obtenerCategoria(BigInteger p) {
        Categorias r;
        CategoriasJpaController pc = new CategoriasJpaController(Persistence.createEntityManagerFactory("BasesFinalPU"));
        r = pc.findCategorias(p);
        return r;
    }

    public long obtenerMarcador(BigInteger b, String nom) {
        AnotacionxpartidoJpaController pc = new AnotacionxpartidoJpaController(Persistence.createEntityManagerFactory("BasesFinalPU"));
        return pc.obtenerMarcador(b, nom);
    }

    public List<Anotacionxpartido> obtenerDetalle(BigInteger b) {
        AnotacionxpartidoJpaController pc = new AnotacionxpartidoJpaController(Persistence.createEntityManagerFactory("BasesFinalPU"));
        return pc.obtenerDetalle(b);
    }

    public List<Jugador> obtenerJugadorE(String nombreeq) {
        JugadorJpaController j = new JugadorJpaController(Persistence.createEntityManagerFactory("BasesFinalPU"));
        List<Jugador> r = j.obtenerJugadorE(nombreeq);
        return r;
    }

    public void insertarAnotacion(Anotacionxpartido a) throws Exception {
        AnotacionxpartidoJpaController pc = new AnotacionxpartidoJpaController(Persistence.createEntityManagerFactory("BasesFinalPU"));
        pc.create(a);
    }

    public List<Fase> obtenerFases() {
        FaseJpaController fc = new FaseJpaController(Persistence.createEntityManagerFactory("BasesFinalPU"));
        return fc.findFaseEntities();
    }

    public Hashtable<String, Partidos> calcularFase(String F) {
        Hashtable<String, Partidos> fas = new Hashtable<String, Partidos>();
        FaseJpaController fc = new FaseJpaController(Persistence.createEntityManagerFactory("BasesFinalPU"));
        Fase au = fc.findFase(F);
        List<Partidos> grupo = new ArrayList<Partidos>();

        return fas;
    }

    public List<Alineaciones> obtenerAlin(String eq, int par) {
        List<Alineaciones> alin = null;
        AlineacionesJpaController fc = new AlineacionesJpaController(Persistence.createEntityManagerFactory("BasesFinalPU"));
        alin = fc.findAlineacionesEq(eq, par);
        return alin;
    }

    public Estadio getEstadio(int cod) {
        Estadio e = null;
        EstadioJpaController ej = new EstadioJpaController(Persistence.createEntityManagerFactory("BasesFinalPU"));
        e = ej.findEstadio(BigInteger.valueOf(cod));
        return e;
    }

    public Jugador encontrarJugador(String j) {
        Jugador e = null;
        JugadorJpaController jug = new JugadorJpaController(Persistence.createEntityManagerFactory("BasesFinalPU"));
        e = jug.encontrarJugadorN(j);
        return e;
    }

    public Precio getPrecio(BigInteger c, String f) {
        PrecioPK p = new PrecioPK();
        p.setCodcategoria(c);
        p.setFase(f);
        PrecioJpaController ej = new PrecioJpaController(Persistence.createEntityManagerFactory("BasesFinalPU"));
        return ej.findPrecio(p);

    }

    public void insertarPartido(Partidos p) {
        PartidosJpaController pc = new PartidosJpaController(Persistence.createEntityManagerFactory("BasesFinalPU"));

        try {
            pc.create(p);
        } catch (Exception ex) {
            Logger.getLogger(Fachada.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public List<Partidos> getPartidosFase(Fase f) {
        PartidosJpaController pa;
        pa = new PartidosJpaController(Persistence.createEntityManagerFactory("BasesFinalPU"));
        List<Partidos> parts = null;
        try {

            parts = pa.getPartidosFase(f);

        } catch (Exception ex) {
            Logger.getLogger(Fachada.class.getName()).log(Level.SEVERE, null, ex);
        }
        return parts;

    }

    public Fase buscarFase(String grupos) {
        Fase fa = null;
        FaseJpaController fc = new FaseJpaController(Persistence.createEntityManagerFactory("BasesFinalPU"));
        try {
            fa = fc.findFase(grupos);

        } catch (Exception ex) {
            Logger.getLogger(Fachada.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fa;
    }

    public boolean EliminarPartidos(List<Partidos> parts) {
        PartidosJpaController pc = new PartidosJpaController(Persistence.createEntityManagerFactory("BasesFinalPU"));
        try {
            for (Partidos p : parts) {

                pc.destroy(p.getCodigopartido());
            }
            return true;
        } catch (Exception ex) {
            Logger.getLogger(Fachada.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public Boleteria obtenerBoleta(SillasPK ay, Partidos p) {
        BoleteriaJpaController pc = new BoleteriaJpaController(Persistence.createEntityManagerFactory("BasesFinalPU"));
        return (Boleteria) pc.boletaSP(ay, p);
    }

    public Usuarios usuarioExis(BigDecimal id) {
        UsuariosJpaController us = new UsuariosJpaController(Persistence.createEntityManagerFactory("BasesFinalPU"));
        return us.findUsuarios(id);
    }

    public void insertarUsuarios(Usuarios nuevo) {
        UsuariosJpaController us = new UsuariosJpaController(Persistence.createEntityManagerFactory("BasesFinalPU"));
        try {
            us.create(nuevo);
        } catch (Exception ex) {
            Logger.getLogger(Fachada.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void actualizarUsuarios(Usuarios nuevo) {
        UsuariosJpaController us = new UsuariosJpaController(Persistence.createEntityManagerFactory("BasesFinalPU"));
        try {
            us.edit(nuevo);
        } catch (Exception ex) {
            Logger.getLogger(Fachada.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void insertarTC(Tarjetacredito e) {
        TarjetacreditoJpaController us = new TarjetacreditoJpaController(Persistence.createEntityManagerFactory("BasesFinalPU"));
        try {
            us.create(e);
        } catch (Exception ex) {
            Logger.getLogger(Fachada.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void insertarPago(Pago pag) {
        PagoJpaController us = new PagoJpaController(Persistence.createEntityManagerFactory("BasesFinalPU"));
        try {
            us.create(pag);
        } catch (Exception ex) {
            Logger.getLogger(Fachada.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void insertarEfectivo(Efectivo e) {
        EfectivoJpaController us = new EfectivoJpaController(Persistence.createEntityManagerFactory("BasesFinalPU"));
        try {
            us.create(e);
        } catch (Exception ex) {
            Logger.getLogger(Fachada.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean existeTarjeta(TarjetacreditoPK t) {
        TarjetacreditoJpaController ta = new TarjetacreditoJpaController(Persistence.createEntityManagerFactory("BasesFinalPU"));
        if (ta.findTarjetacredito(t) != null) {
            return true;
        } else {
            return false;
        }
    }

    public ImageIcon obtenerImagen(int id) {
        JugadorJpaController pc = new JugadorJpaController(Persistence.createEntityManagerFactory("BasesFinalPU"));
        BufferedImage image;
        ImageIcon icon = null;
        try {
            Jugador j = pc.jugadorxCodig(id);
            System.out.println(j.getFoto().getClass().getName());
            byte[] b = j.getFoto();
            //InputStream in = b.getBinaryStream(0,b.length());
            image = ImageIO.read(new ByteArrayInputStream(b));
            icon = new ImageIcon(image);
        } catch (Exception ex) {
            //System.out.println(ex.getMessage());
            Logger.getLogger(Fachada.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return icon;
    }
}
