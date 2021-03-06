"use strict";

module.exports = {
  up: function(migration, DataTypes, done) {
    migration
      .createTable('Cards', {
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
          allowNull: true
        },
        text: DataTypes.STRING,
        type: DataTypes.STRING,
        createdAt: DataTypes.DATE,
        updatedAt: DataTypes.DATE
      })
      .complete(done);
  },

  down: function(migration, DataTypes, done) {
    migration
      .dropTable('Cards')
      .complete(done);
  }
};