package grails

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class HistoriquePersonnelController {

    IHistoriquePersonnelService historiquePersonnelService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond historiquePersonnelService.list(params), model:[historiquePersonnelCount: historiquePersonnelService.count()]
    }

    def show(Long id) {
        respond historiquePersonnelService.get(id)
    }

    def create() {
        respond new HistoriquePersonnel(params)
    }

    def save(HistoriquePersonnel historiquePersonnel) {
        if (historiquePersonnel == null) {
            notFound()
            return
        }

        try {
            historiquePersonnelService.save(historiquePersonnel)
        } catch (ValidationException e) {
            respond historiquePersonnel.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'historiquePersonnel.label', default: 'HistoriquePersonnel'), historiquePersonnel.id])
                redirect historiquePersonnel
            }
            '*' { respond historiquePersonnel, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond historiquePersonnelService.get(id)
    }

    def update(HistoriquePersonnel historiquePersonnel) {
        if (historiquePersonnel == null) {
            notFound()
            return
        }

        try {
            historiquePersonnelService.save(historiquePersonnel)
        } catch (ValidationException e) {
            respond historiquePersonnel.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'historiquePersonnel.label', default: 'HistoriquePersonnel'), historiquePersonnel.id])
                redirect historiquePersonnel
            }
            '*'{ respond historiquePersonnel, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        historiquePersonnelService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'historiquePersonnel.label', default: 'HistoriquePersonnel'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'historiquePersonnel.label', default: 'HistoriquePersonnel'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
