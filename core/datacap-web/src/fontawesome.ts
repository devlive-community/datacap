import {FontAwesomeIcon} from '@fortawesome/vue-fontawesome'
import {library} from '@fortawesome/fontawesome-svg-core'
import {faCircle, faColumns, faDatabase, faDroplet, faFilter, faKey, faRepeat, faTable} from '@fortawesome/free-solid-svg-icons'

/**
 * Creates icons for the given app.
 *
 * @param {any} app - The app object.
 */
const createIcons = (app: any) => {
  library.add(faFilter, faDatabase, faTable, faColumns, faKey, faRepeat, faDroplet, faCircle)
  app.component('FontAwesomeIcon', FontAwesomeIcon)
}

export {
  createIcons
}
