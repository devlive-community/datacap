import { createStore } from 'vuex'

const store = createStore({
    state: {
        data: null
    },
    mutations: {
        setData(state, payload)
        {
            state.data = payload
        }
    },
    getters: {
        getData(state)
        {
            return state.data
        }
    },
    actions: {
        updateData({commit}, payload)
        {
            commit('setData', payload)
        }
    }
})

export default store
