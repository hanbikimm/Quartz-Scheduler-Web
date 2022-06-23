export const Member = {
    state: {
        name: '',
        username: '',
        email: '',
    },

    mutations: {
      setUser(state, result){
        state.name = result.name;
        state.username = result.username;
        state.email = result.email;
      },

      removeUser(state){
        state.name = '';
        state.username = '';
        state.email = '';
      }
    },

    actions: {
    },

    getters: {
      isLogin(state){
        return state.name !== '';
      }
    }
  };