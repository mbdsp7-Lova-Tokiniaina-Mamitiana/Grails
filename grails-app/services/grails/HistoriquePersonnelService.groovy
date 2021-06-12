package grails

import grails.gorm.transactions.Transactional

@Transactional
class HistoriquePersonnelService implements IHistoriquePersonnelService{

    def serviceMethod() {

    }

    @Override
    HistoriquePersonnel get(Serializable id) {
        return null
    }

    @Override
    List<HistoriquePersonnel> list(Map args) {
        return HistoriquePersonnel.findAll(args)
    }

    @Override
    Long count() {
        return null
    }

    @Override
    void delete(Serializable id) {

    }

    @Override
    HistoriquePersonnel save(HistoriquePersonnel historiquePersonnel) {
        return historiquePersonnel.save()
    }
    List<HistoriquePersonnel> listPerso(Map args,String idUser) {
        return HistoriquePersonnel.findAllByIdUser(idUser,args)
    }
    List<HistoriquePersonnel> listParPari(Map args,String idPari) {
        return HistoriquePersonnel.findAllByIdPari(idPari,args)
    }
    List<HistoriquePersonnel> listParMatch(Map args,String idMatch) {
        return HistoriquePersonnel.findAllByIdMatch(idMatch,args)
    }
    boolean  distrution(String idPari){
        ProfilService profilService =new ProfilService()
        def p = [
                max: Integer.MAX_VALUE,
                offset: 0
        ]
        List<HistoriquePersonnel> list = listParPari(p,idPari)
        for (HistoriquePersonnel h:list){
            profilService.addSolde(h.getIdUser(),h.getCote()*h.getMontant())
        }
        return true

    }

}
