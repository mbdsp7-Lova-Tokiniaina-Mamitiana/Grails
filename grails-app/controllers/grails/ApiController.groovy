package grails

import grails.converters.JSON
import grails.converters.XML

import javax.servlet.http.HttpServletResponse

class ApiController {
    ProfilService profilService
    HistoriquePersonnelService histoService
    def profil() {
        switch (request.getMethod()) {
            case "GET":
                if (!params.id)
                    return response.status = HttpServletResponse.SC_BAD_REQUEST
                def profilInstance = profilService.get(params.id)
                if (!profilInstance)
                    return response.status = HttpServletResponse.SC_NOT_FOUND

                serializeData(profilInstance, request.getHeader("Accept"))
                break
            case "POST":
                if (!params.id)
                    return response.status = HttpServletResponse.SC_BAD_REQUEST
                def profilInstance = profilService.get(params.id)
                if (!profilInstance)
                    return response.status = HttpServletResponse.SC_NOT_FOUND
                profilService.addSolde(params.id,Double.parseDouble(params.montant))
                break
            default:
                return response.status = HttpServletResponse.SC_METHOD_NOT_ALLOWED
                break

        }
        return response.status = HttpServletResponse.SC_NOT_ACCEPTABLE
    }
    def profils() {
        switch (request.getMethod()) {

            case "POST":
                def profil = new Profile();
                profil.setIduser(""+params.id);
                profil.setSolde(0);
                profil.setNom(params.nom);
                profil.setPrenom(params.prenom)
                profilService.save(profil)
                return response.status = HttpServletResponse.SC_OK
               break
            default:
                return response.status = HttpServletResponse.SC_METHOD_NOT_ALLOWED
                break

        }
        return response.status = HttpServletResponse.SC_NOT_ACCEPTABLE
    }
    def historiquepersonnels(){

        switch (request.getMethod()) {
            case "GET":

                def historiques = histoService.listPerso(params,params.id)
                serializeData(historiques, request.getHeader("Accept"))
                break
            default:
                return response.status = HttpServletResponse.SC_METHOD_NOT_ALLOWED
                break
        }
        return response.status = HttpServletResponse.SC_NOT_ACCEPTABLE
    }
    def distribution(){
        switch (request.getMethod()) {
            case "POST":


                serializeData(params.id, request.getHeader("Accept"))
                break
            default:
                return response.status = HttpServletResponse.SC_METHOD_NOT_ALLOWED
                break
        }
        return response.status = HttpServletResponse.SC_NOT_ACCEPTABLE

    }

    def serializeData(object, format)
    {
        switch (format)
        {
            case 'json':
            case 'application/json':
            case 'text/json':
                render object as JSON
                break
            case 'xml':
            case 'application/xml':
            case 'text/xml':
                render object as XML
                break
        }
    }
}
