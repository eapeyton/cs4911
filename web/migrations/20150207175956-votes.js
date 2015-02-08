"use strict";

module.exports = {
  up: function(migration, DataTypes, done) {
    migration
      .createTable('Votes', {
        id: { 
          type:  DataTypes.UUIDV4, 
          defaultValue: DataTypes.UUIDV4,
          unique: true,
          primaryKey: true
        },
        uid: {
          type: DataTypes.UUIDV4,
          references: 'Users',
          referencesKey: 'id',
          allowNull: false
        },
        cid: {
          type: DataTypes.UUIDV4,
          references: 'Cards',
          referencesKey: 'id',
          allowNull: false
        },
        upvoted: DataTypes.BOOLEAN,
        createdAt: DataTypes.DATE,
        updatedAt: DataTypes.DATE
      })
      .complete(done);
  },

  down: function(migration, DataTypes, done) {
    migration
      .dropTable('Votes')
      .complete(done);
  }
};