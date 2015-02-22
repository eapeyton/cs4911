"use strict";

module.exports = {
  up: function(migration, DataTypes, done) {
    migration
      .createTable('Rounds', {
        id: { 
          type:  DataTypes.UUID, 
          defaultValue: DataTypes.UUIDV4,
          unique: true,
          primaryKey: true
        },
        gameId: {
          type: DataTypes.UUID,
          references: 'Games',
          referencesKey: 'id',
          allowNull: false
        },
        judge: {
          type: DataTypes.UUID,
          references: 'Users',
          referencesKey: 'id',
          allowNull: false
        },
        blackCard: {
          type: DataTypes.UUID,
          references: 'Cards',
          referencesKey: 'id',
          allowNull: false
        },
        winner: {
          type: DataTypes.UUID,
          references: 'Users',
          referencesKey: 'id'
        },
        winningCard: {
          type: DataTypes.UUID,
          references: 'Cards',
          referencesKey: 'id'
        },
        state: DataTypes.STRING,
        createdAt: DataTypes.DATE,
        updatedAt: DataTypes.DATE
      })
      .complete(done);
  },

  down: function(migration, DataTypes, done) {
    migration
      .dropTable('Rounds')
      .complete(done);
  }
};