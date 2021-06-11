package grails

import grails.gorm.services.Service

@Service(HistoriquePersonnel)
interface IHistoriquePersonnelService {

    HistoriquePersonnel get(Serializable id)

    List<HistoriquePersonnel> list(Map args)

    Long count()

    void delete(Serializable id)

    HistoriquePersonnel save(HistoriquePersonnel historiquePersonnel)

}