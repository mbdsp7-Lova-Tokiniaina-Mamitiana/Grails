package grails

import grails.converters.JSON
import grails.converters.XML

import javax.servlet.http.HttpServletResponse
import java.sql.Timestamp
import java.text.SimpleDateFormat

class ApiController {
    ProfilService profilService
    HistoriquePersonnelService historiquePersonnelService

    def profil() {
        switch (request.getMethod()) {
            case "GET":
                //GET PROFIL PAR IDUSER = params.id
                if (!params.id)
                    return response.status = HttpServletResponse.SC_BAD_REQUEST
                def profilInstance = profilService.get(""+params.id)
                if (!profilInstance)
                    return response.status = HttpServletResponse.SC_NOT_FOUND

                serializeData(profilInstance, request.getHeader("Accept"))
                break
            case "POST":
                //AJOUT DE SOLDE params IDUSER=params.id et montant = params.montant
                if (!params.id)
                    return response.status = HttpServletResponse.SC_BAD_REQUEST
                def profilInstance = profilService.get(""+params.id)
                if (!profilInstance)
                    return response.status = HttpServletResponse.SC_NOT_FOUND
                if(Double.parseDouble(""+params.montant)<0){
                    response.sendError(403,"Le montant doit etre positif");
                }
                profilService.addSolde(params.id,Double.parseDouble(""+params.montant))
                return response.status = HttpServletResponse.SC_OK
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
                //CREATION Profil "Id user = params.id"
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
                //Omeo Id User params{id} sy params.status
                def historiques = historiquePersonnelService.listPerso(params,""+params.id)

                serializeData(historiques, request.getHeader("Accept"))
                break
            case "POST":
                //CREATION Historique personnel : dates en Long
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm")
                HistoriquePersonnel historiquePersonnel=new HistoriquePersonnel();
                historiquePersonnel.setIdMatch(""+params.idmatch)
                historiquePersonnel.setIdPari(""+params.idpari)
                historiquePersonnel.setIdUser(""+params.iduser)
                historiquePersonnel.setCote(Double.parseDouble(""+params.cote))
                historiquePersonnel.setNomEquipe1(""+params.equipe1)
                historiquePersonnel.setNomEquipe2(""+params.equipe2)
                historiquePersonnel.setTextePari(""+params.textpari)
                historiquePersonnel.setMontant(Double.parseDouble(params.montant))
                historiquePersonnel.setLocalisationx(Double.parseDouble(params.localisationx))
                historiquePersonnel.setLocalisationy(Double.parseDouble(params.localisationy))
                Date d1=format.parse(params.date)
                Date d2=format.parse(params.dateHisto)
                historiquePersonnel.setDateMatch(new Timestamp(d1.getTime()))
                historiquePersonnel.setAvatarEquipe1(params.avatar1)
                historiquePersonnel.setAvatarEquipe2(params.avatar2)
                historiquePersonnel.setDateHisto(new Timestamp(d2.getTime()))
                historiquePersonnel.setStatut(0)
                historiquePersonnel.setGain(0)
                Profile profile = profilService.get(params.iduser);
                if(profile!=null){
                    if(profile.getSolde()<historiquePersonnel.getMontant()){
                        response.sendError(403,"Solde insuffisant");
                    }else{
                        profilService.addSolde(params.iduser,-historiquePersonnel.getMontant())
                        historiquePersonnelService.save(historiquePersonnel)
                    }

                }




                return response.status = HttpServletResponse.SC_OK
                break
            default:
                return response.status = HttpServletResponse.SC_METHOD_NOT_ALLOWED
                break
        }
        return response.status = HttpServletResponse.SC_NOT_ACCEPTABLE
    }
    def counthistoriquepersonnels(){

        switch (request.getMethod()) {
            case "GET":
                //Omeo Id User params{id} sy params.status
                def count = historiquePersonnelService.countListPerso(params,""+params.id)
                String result = "{count:"+count+"}"
                HashMap jsonMap = new HashMap()
                jsonMap.put("count",count)
                serializeData(jsonMap, request.getHeader("Accept"))

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
                //Distribution des gains d'un pari = params.id =idpari
                def jsonObject = request.JSON;
                String idpari = jsonObject.id;
                if(idpari == null){
                    idpari = params.id;
                }

                historiquePersonnelService.distrution(idpari)
                return response.status = HttpServletResponse.SC_OK
                break
            default:
                return response.status = HttpServletResponse.SC_METHOD_NOT_ALLOWED
                break
        }
        return response.status = HttpServletResponse.SC_NOT_ACCEPTABLE

    }
    def terminermatch(){
        switch (request.getMethod()) {
            case "POST":
                //Distribution des gains d'un pari = params.id =imatch
                def jsonObject = request.JSON;
                String idmatch = jsonObject.id;
                if(idmatch == null){
                    idmatch = params.id;
                }
                historiquePersonnelService.terminerMatch(idmatch)
                return response.status = HttpServletResponse.SC_OK
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
            default:
                render object as JSON
                break
        }
    }
}
