module.exports = function(sequelize, DataTypes) {
  var PlayedCard = sequelize.define("PlayedCard", {
    id: { 
      type: DataTypes.UUID,
      defaultValue: DataTypes.UUIDV4,
      unique: true,
      primaryKey: true
    },
    userId: {
      type: DataTypes.UUID,
      references: 'Users',
      referencesKey: 'id',
      allowNull: false,
      unique: 'userCardRoundIndex'
    },
    cardId: {
      type: DataTypes.UUID,
      references: 'Cards',
      referencesKey: 'id',
      allowNull: false,
      unique: 'userCardRoundIndex'
    },
    roundId: {
      type: DataTypes.UUID,
      references: 'Rounds',
      referencesKey: 'id',
      allowNull: false,
      unique: 'userCardRoundIndex'
    },
    createdAt: DataTypes.DATE,
    updatedAt: DataTypes.DATE
  }, {
    classMethods: {
      associate: function(models) {
        PlayedCard.belongsTo(models.User, { foreignKey: 'userId' });
        PlayedCard.belongsTo(models.Card, { foreignKey: 'cardId' });
        PlayedCard.belongsTo(models.Round, { foreignKey: 'roundId' });
      }
    }
  });

  return PlayedCard;
};