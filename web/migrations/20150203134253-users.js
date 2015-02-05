"use strict";

module.exports = {
  up: function(migration, DataTypes, done) {
    migration
      .createTable('Users', {
        id: { 
          type:  DataTypes.UUIDV4, 
          primaryKey: true  
        },
        rid: DataTypes.UUIDV4,
        fbid: DataTypes.STRING,
        fbToken: DataTypes.STRING,
        name: DataTypes.STRING,
        pic: DataTypes.STRING,
        createdAt: DataTypes.DATE,
        updatedAt: DataTypes.DATE
      })
      .complete(done)
    done();
  },

  down: function(migration, DataTypes, done) {
    migration
      .dropTable('Users')
      .complete(done)
    done();
  }
};
