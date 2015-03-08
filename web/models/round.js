module.exports = function(sequelize, DataTypes) {
  var Round = sequelize.define("Round", {
    id: { 
      type: DataTypes.UUID,
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
  }, {
    classMethods: {
      associate: function(models) {
        Round.belongsTo(models.Game, { foreignKey: 'gameId' });
        Round.belongsTo(models.User, { foreignKey: 'judge' });
        Round.belongsTo(models.Card, { foreignKey: 'blackCard' });
        Round.belongsTo(models.User, { as:'winner', foreignKey: 'winner' });
        Round.belongsTo(models.Card, { foreignKey: 'winningCard' });
        Round.hasMany(models.PlayedCard, { foreignKey: 'roundId' });
      }
    }
  });

  return Round;
};