"use strict";

module.exports = {
  up: function(migration, DataTypes, done) {
    migration
      .createTable('PlayerStates', {
        id: { 
          type:  DataTypes.UUID, 
          defaultValue: DataTypes.UUIDV4,
          unique: true,
          primaryKey: true
        },
        userId: {
          type: DataTypes.UUID,
          references: 'Users',
          referencesKey: 'id',
          allowNull: false,
          unique: 'userGameIndex'
        },
        gameId: {
          type: DataTypes.UUID,
          references: 'Games',
          referencesKey: 'id',
          allowNull: false,
          unique: 'userGameIndex'
        },
        state: DataTypes.STRING,
        createdAt: DataTypes.DATE,
        updatedAt: DataTypes.DATE
      })
      .complete(done);
  },

  down: function(migration, DataTypes, done) {
    migration
      .dropTable('PlayerStates')
      .complete(done);
  }
};