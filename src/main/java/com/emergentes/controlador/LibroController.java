
package com.emergentes.controlador;

import com.emergentes.modelo.Categoria;
import com.emergentes.modelo.Libros;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "LibroController", urlPatterns = {"/LibroController"})
public class LibroController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String op = (request.getParameter("op").equals(""))?"listar": request.getParameter("op");
        Libros prod = new Libros();
        int id;
        
        HttpSession ses = request.getSession();
        List<Libros> lista = (List<Libros>) ses.getAttribute("biblioteca");
        List<Categoria> categorias =(List<Categoria>) ses.getAttribute("cates");
        
        switch(op){
            case "nuevo":
                if(lista.size()==0){
                    prod.setId(1);
                }
                else{
                    prod.setId(lista.get(lista.size()-1).getId()+1);
                }
                request.setAttribute("categorias",categorias);
                request.setAttribute("prod",prod);
                request.setAttribute("tipo", "new");
                request.getRequestDispatcher("libro-edit.jsp").forward(request, response);
                break;
            case "editar":
                id = Integer.parseInt(request.getParameter("id"));
                prod = lista.get(posNodoLibro(id,request));
                request.setAttribute("categorias",categorias);
                request.setAttribute("prod",prod);
                request.setAttribute("tipo", "edit");
                request.getRequestDispatcher("libro-edit.jsp").forward(request, response);
                break;
            case "eliminar":
                id = Integer.parseInt(request.getParameter("id"));
                int pos = posNodoLibro(id,request);
                lista.remove(pos);
                response.sendRedirect("libros.jsp");
                break;
            case "listar":
                request.setAttribute("biblioteca", lista);
                response.sendRedirect("libros.jsp");
             

        }
        
        
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String titulo = request.getParameter("titulo");
        String autor = request.getParameter("autor");
        String disponible = request.getParameter("disponible");
        int idcat = Integer.parseInt(request.getParameter("idcat"));
        Categoria cate = nodoCategoria(idcat, request);
        
        String tipo = request.getParameter("tipo");
        
        Libros p= new Libros();
        p.setId(id);
        p.setTitulo(titulo);
        p.setAutor(autor);
        p.setDisponible(disponible);
        p.setCate(cate);
        
        HttpSession ses = request.getSession();
        List<Libros> biblioteca = (List<Libros>)ses.getAttribute("biblioteca");
        
        if(tipo.equals("new")){
            biblioteca.add(p);
        }
        else{
            biblioteca.set(posNodoLibro(id,request), p);
        }
        response.sendRedirect("libros.jsp");
       
    }
    public int posNodoLibro(int id, HttpServletRequest request){
        int index = -1;
        
        HttpSession ses = request.getSession();
        List<Libros> lista = (List<Libros>)ses.getAttribute("biblioteca");
        for (int i=0; i<lista.size(); i++){
            if(lista.get(i).getId()== id){
                index =i;
                break;
            }
        }
        return index;
    }
    
    public Categoria nodoCategoria(int id, HttpServletRequest request){
        Categoria aux = new Categoria();
        HttpSession ses = request.getSession();
        List<Categoria> lista = (List<Categoria>)ses.getAttribute("cates");
        for(Categoria obj : lista){
            if(obj.getId()==id){
                aux=obj;
            }
            
        }return aux;
    }

}
