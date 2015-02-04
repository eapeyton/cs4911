"use strict";

module.exports = {
  up: function(migration, DataTypes, done) {
    migration
      .createTable('BlackCards', {
        id: { 
          type:  DataTypes.UUIDV4, 
          primaryKey: true  
        },
        text: DataTypes.STRING,
        creator: DataTypes.UUIDV4,
        createdAt: DataTypes.DATE,
        updatedAt: DataTypes.DATE
      })
      .complete(done)
    done();
  },

  down: function(migration, DataTypes, done) {
    migration
      .dropTable('BlackCards')
      .complete(done)
    done();
  }
};
