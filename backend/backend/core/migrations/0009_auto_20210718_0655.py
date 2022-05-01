# Generated by Django 3.2.4 on 2021-07-18 06:55

from django.db import migrations, models
import uuid


class Migration(migrations.Migration):

    dependencies = [
        ('core', '0008_auto_20210718_0654'),
    ]

    operations = [
        migrations.AlterField(
            model_name='itap',
            name='activation_key',
            field=models.CharField(blank=True, default='5f835975', editable=False, max_length=8),
        ),
        migrations.AlterField(
            model_name='itap',
            name='seed',
            field=models.CharField(default=uuid.UUID('e03b06c2-efea-41c3-b0d8-f3f0bbe0a43f'), max_length=36),
        ),
    ]
