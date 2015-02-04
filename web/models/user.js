module.exports = function(sequelize, DataTypes) {
  var User = sequelize.define("User", {
    id: { type: DataTypes.UUID, defaultValue: DataTypes.UUIDV4 },
    username: DataTypes.STRING,
    fbToken: DataTypes.STRING,
    createdAt: DataTypes.DATE,
    updatedAt: DataTypes.DATE
  }, {
    classMethods: {
      associate: function(models) {
        User.belongsTo(models.Room, { foreignKey: 'rid' });
        User.hasMany(models.WhiteCard, { foreignKey: 'creator' });
        User.hasMany(models.BlackCard, { foreignKey: 'creator' });
      }
    }
  });

  return User;
};