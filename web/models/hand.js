module.exports = function(sequelize, DataTypes) {
  var Hand = sequelize.define("Hand", {
    id: { 
      type: DataTypes.UUID,
      defaultValue: DataTypes.UUIDV4,
      unique: true,
      primaryKey: true
    },
    cardId: {
      type: DataTypes.UUID,
      references: 'Cards',
      referencesKey: 'id',
      allowNull: false
    },
    gameId: {
      type: DataTypes.UUID,
      references: 'Games',
      referencesKey: 'id',
      allowNull: false
    },
    userId: {
      type: DataTypes.UUID,
      references: 'Users',
      referencesKey: 'id',
      allowNull: false
    },
    played: DataTypes.BOOLEAN,
    createdAt: DataTypes.DATE,
    updatedAt: DataTypes.DATE
  }, {
    classMethods: {
      associate: function(models) {
        Hand.belongsTo(models.User, { foreignKey: 'userId' });
        Hand.belongsTo(models.Card, { foreignKey: 'cardId' });
        Hand.belongsTo(models.Game, { foreignKey: 'gameId' });
        Hand.hasOne(models.PlayerState, { foreignKey: 'handId' });
      }
    }
  });

  return Hand;
};